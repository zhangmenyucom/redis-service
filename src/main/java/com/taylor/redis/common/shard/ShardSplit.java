package com.taylor.redis.common.shard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.taylor.redis.common.util.RedisMasterProber;
import com.taylor.redis.exception.RedisMasterShardNotExistsException;
import com.taylor.redis.exception.RedisResourceInitException;
import com.taylor.redis.exception.RedisShardConnectException;

import lombok.extern.log4j.Log4j2;
import redis.clients.jedis.JedisPool;
import redis.clients.util.MurmurHash;
import redis.clients.util.SafeEncoder;

/**
 * 类RedisShardSplit.java的实现描述：
 * 
 */
@Log4j2
public class ShardSplit implements RedisShardSplit<JedisPool, RedisMasterSlaverGroup> {

	private static ShardSplit instance;

	private Pattern tagPattern = null;
	public static final Pattern DEFAULT_KEY_TAG_PATTERN = Pattern.compile("\\{(.+?)\\}");

	/**
	 * hash the resource
	 */
	private final static MurmurHash MURMUR_HASH = new MurmurHash();
	private volatile int SHARD_NUM = 0;

	private Map<Integer, Integer> group2Hash = new ConcurrentHashMap<Integer, Integer>();

	private Map<Integer, RedisMasterSlaverGroup> nodes = new ConcurrentHashMap<Integer, RedisMasterSlaverGroup>();

	private final Map<RedisMasterSlaverGroup, JedisPool> resources = new ConcurrentHashMap<RedisMasterSlaverGroup, JedisPool>();

	/**
	 * GenericObjectPool config
	 */
	private GenericObjectPoolConfig poolConfig;

	/**
	 * Singleton
	 * 
	 * @param shards
	 * @param poolConfig
	 * @return
	 */
	public static ShardSplit getInstance(List<RedisMasterSlaverGroup> shards, GenericObjectPoolConfig poolConfig) {

		return getInstance(shards, poolConfig, null);
	}

	/**
	 * Singleton
	 * 
	 * @param shards
	 * @param poolConfig
	 * @param tagPattern
	 * @return
	 */
	public static ShardSplit getInstance(List<RedisMasterSlaverGroup> shards, GenericObjectPoolConfig poolConfig, Pattern tagPattern) {
		if (null == instance) {
			synchronized (ShardSplit.class) {
				if (null == instance) {
					instance = new ShardSplit(shards, poolConfig, tagPattern);
				}
			}
		}
		return instance;
	}

	/**
	 * with 64-bits not 128
	 * 
	 * @param shards
	 * @param poolConfig
	 * @param tagPattern
	 */
	private ShardSplit(List<RedisMasterSlaverGroup> shards, GenericObjectPoolConfig poolConfig, Pattern tagPattern) {
		this.tagPattern = tagPattern;
		this.poolConfig = poolConfig;
		initResources(shards);
	}

	public void initResources(List<RedisMasterSlaverGroup> shards) {
		if (CollectionUtils.isEmpty(shards)) {
			return;
		}
		SHARD_NUM = SHARD_NUM < 0 ? 0 : SHARD_NUM;
		// 进行排序，按照序列对应hash + 1值
		try {
			Collections.sort(shards, new Comparator<RedisMasterSlaverGroup>() {
				public int compare(RedisMasterSlaverGroup shard1, RedisMasterSlaverGroup shard2) {
					return shard1.getId().compareTo(shard2.getId());
				}
			});
			// 创建连接池，每一个分片服务器对应一个连接池
			for (int i = 0; i != shards.size(); ++i) {
				final RedisMasterSlaverGroup groupInfo = shards.get(i);
				// INIT ID
				group2Hash.put(groupInfo.getId(), Integer.valueOf(1 + i));
				nodes.put(Integer.valueOf(1 + i), groupInfo);
				resources.put(groupInfo, createReaource(groupInfo, poolConfig));
				SHARD_NUM++;
			}
		} catch (Throwable e) {
			log.error(new RedisResourceInitException("redis resources init exception", e));
		}

	}

	/**
	 * 对RedisMasterSlaverGroup的主库，创建jedisPool，
	 * 
	 * @param groupInfo
	 * @param poolConfig
	 * @return
	 * @throws RedisMasterShardNotExistsException
	 * @throws RedisShardConnectException
	 */
	private JedisPool createReaource(RedisMasterSlaverGroup groupInfo, GenericObjectPoolConfig poolConfig) throws RedisMasterShardNotExistsException, RedisShardConnectException {
		RedisShardInfo master = groupInfo.getMaster();
		if (master == null) {
			throw new RedisMasterShardNotExistsException(" master not exist in group : " + groupInfo.getId());
		}
		try {
			if (master.getPassword() != null && master.getPassword().trim().length() > 0) {
				return new JedisPool(poolConfig, master.getHost(), master.getPort(), master.getTimeout(), master.getPassword());
			}
			return new JedisPool(poolConfig, master.getHost(), master.getPort(), master.getTimeout());
		} catch (Throwable e) {
			throw new RedisShardConnectException(groupInfo.getName() + " redis shard server create failed! ", e);
		}
	}

	/**
	 * 1. 重建资源库,未重新计算分片总数，以免数据分片出错，造成脏数据， TODO 实现扩容方案时，需要重构该部分代码
	 */
	private volatile ExecutorService executor = null;

	public void reBuildResources(final List<RedisMasterSlaverGroup> shardGroups) {
		if (CollectionUtils.isEmpty(shardGroups)) {
			return;
		}
		/**
		 * 2. 避免任务失败时，多次重复创建线程。
		 */
		if (executor != null) {
			return;
		}
		synchronized (this) {
			// double check
			if (executor != null) {
				return;
			}
			executor = Executors.newSingleThreadExecutor();
		}
		try {
			// 1.restore the wrong server submitted
			executor.submit(new Restorer(shardGroups));
			// 2. check all resource and restore if master has switched
			executor.submit(new Restorer(nodes.values()));
		} catch (Throwable e) {
			throw new RedisResourceInitException("redis resources init exception", e);
		} finally {
			if (executor != null) {
				executor.shutdown();
				executor = null;
			}
		}

	}

	/**
	 * 检查并重置出错的分片组的master，不重新计算分片总数，以免数据分片出错，造成脏数据， TODO 实现扩容方案时，需要重构该部分代码
	 */
	private class Restorer implements Callable<RedisMasterSlaverGroup> {

		private List<RedisMasterSlaverGroup> shardGroups;

		public Restorer(Collection<RedisMasterSlaverGroup> groups) {
			this.shardGroups = new ArrayList<RedisMasterSlaverGroup>();
			this.shardGroups.addAll(groups);
		}

		public RedisMasterSlaverGroup call() throws Exception {
			ExecutorService probeExecutor = Executors.newFixedThreadPool(shardGroups.size());

			try {

				ConcurrentLinkedQueue<Future<RedisMasterSlaverGroup>> probeResult = new ConcurrentLinkedQueue<Future<RedisMasterSlaverGroup>>();
				for (RedisMasterSlaverGroup group : shardGroups) {
					probeResult.add(probeExecutor.submit(new RedisMasterProber.Prober(group)));
				}

				for (Future<RedisMasterSlaverGroup> rs : probeResult) {

					RedisMasterSlaverGroup _group = rs.get();
					swithMaster(_group);
				}

			} catch (Throwable e) {
				throw new RedisMasterShardNotExistsException("Discover master server failed,must restart Manually", e);
			} finally {
				if (probeExecutor != null && !probeExecutor.isShutdown())
					probeExecutor.shutdown();
			}
			return null;
		}

	}

	private void swithMaster(RedisMasterSlaverGroup _group) throws RedisMasterShardNotExistsException {

		if (null == group2Hash.get(_group.getId())) {
			throw new RedisMasterShardNotExistsException(_group.toString() + " not exists in resource list");
		}

		String _host = _group.getMaster().getHost();
		// Integer _port = _group.getMaster().getPort();

		RedisMasterSlaverGroup group = nodes.get(_group.getId());
		String host = group.getMaster().getHost();
		Integer port = group.getMaster().getPort();

		if (_host.equals(host) && port.equals(port)) {
			return;
		}

		nodes.put(group2Hash.get(_group.getId()), _group);
		resources.put(_group, createReaource(_group, poolConfig));
	}

	public JedisPool getShardPool(byte[] key) {
		return resources.get(getShardInfo(key));
	}

	public JedisPool getShardPool(String key) {
		return resources.get(getShardInfo(key));
	}

	public RedisMasterSlaverGroup getShardInfo(byte[] key) {
		if (SHARD_NUM == 0) {
			return null;
		}
		// start from 1
		Integer id = (int) (1 + Math.abs(MURMUR_HASH.hash(key) % SHARD_NUM));
		return nodes.get(id);
	}

	public RedisMasterSlaverGroup getShardInfo(String key) {
		return getShardInfo(SafeEncoder.encode(getKeyTag(key)));
	}

	/**
	 * A key tag is a special pattern inside a key that, if preset, is the only
	 * part of the key hashed in order to select the server for this key.
	 * 
	 * @see http://code.google.com/p/redis/wiki/FAQ#I
	 *      'm_using_some_form_of_key_hashing_for_partitioning,_but_wh
	 * @param key
	 * @return The tag if it exists, or the original key
	 */
	public String getKeyTag(String key) {
		if (tagPattern != null) {
			Matcher m = tagPattern.matcher(key);
			if (m.find())
				return m.group(1);
		}
		return key;
	}

	/**
	 * GET ALL CONFIG
	 * 
	 * @see https://github.com/xetorthio/jedis/blob/master/src/main/java/redis/
	 *      clients/util/Sharded.java
	 * @return
	 */
	public Collection<RedisMasterSlaverGroup> getAllShardInfo() {
		return Collections.unmodifiableCollection(nodes.values());
	}

	public void destoryAllResources() {
		if (resources.size() == 0)
			return;
		for (JedisPool pool : resources.values()) {
			try {
				pool.destroy();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}

	}

}
