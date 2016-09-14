package com.taylor.redis.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import com.taylor.redis.common.shard.RedisMasterSlaverGroup;
import com.taylor.redis.common.shard.RedisShardInfo;
import com.taylor.redis.exception.RedisMultiMasterShardException;
import com.taylor.redis.exception.RedisShardConnectException;

import redis.clients.jedis.BinaryJedis;
import redis.clients.jedis.Jedis;

public class RedisMasterProber {

	private final static Logger log = Logger.getLogger(RedisMasterProber.class);

	public static class Prober implements Callable<RedisMasterSlaverGroup> {

		private RedisMasterSlaverGroup shardGroup;
		private Integer groupId;

		public Prober(RedisMasterSlaverGroup shardGroup) {
			this.shardGroup = shardGroup;
			this.groupId = shardGroup.getId();
		}

		public Prober(RedisMasterSlaverGroup shardGroup, Integer groupId) {
			this.shardGroup = shardGroup;
			this.groupId = groupId;
		}

		/**
		 * @desc call(选举master)
		 * @throws Exception
		 * @author xiaolu.zhang
		 * @date 2016年9月14日 下午4:57:03
		 */
		public RedisMasterSlaverGroup call() throws Exception {
			log.info("Discovery the master or slaver server thread start....");
			if (null == shardGroup) {
				return null;
			}
			List<RedisShardInfo> shards = new ArrayList<RedisShardInfo>();

			/* 当前master,初始化时为null */
			final RedisShardInfo master = shardGroup.getMaster();

			/* master存在但不可访问时为true */
			boolean hasMasterButErr = false;
			if (master != null) {
				try {
					// master available , return
					if (isMaster(master))
						return shardGroup;
					else {
						/* 当前分组master不是真master并且可以访问，则放入salver中 */
						ping(master);
						hasMasterButErr = true;
					}
				} catch (Throwable e) {
					hasMasterButErr = true;
				}
			}
			shards.addAll(shardGroup.getSlaverList());
			RedisMasterSlaverGroup group = new RedisMasterSlaverGroup();
			group.setId(groupId);
			do {
				/* 清空从列表，防止失败重试 */
				group.getSlaverList().clear();

				/* 第一步从slaverList中选出一个master */
				if (CollectionUtils.isNotEmpty(shards)) {
					for (RedisShardInfo shard : shards) {
						try {
							if (isMaster(shard)) {
								// 有两个不同的master
								if (!hasMasterButErr && group.getMaster() != null && !shard.equals(group.getMaster())) {
									throw new RedisMultiMasterShardException("一个组里存在多个master : " + shardGroup.getId());
								}
								group.setMaster(shard);
							} else {
								group.addSlaver(shard);
							}
						} catch (Throwable e) {
							log.error(new RedisShardConnectException("获取不到redis的role,直接放入salver中" + shard.toString(), e), e);
							group.addSlaver(shard);
						}
					}
				}

				/* 第2步 出现异常,未找master则0.5秒后重试，以等待从库切换为主库 */
				if (hasMasterButErr && group.getMaster() == null) {
					try {
						if (isMaster(master)) {
							hasMasterButErr = false;
							return shardGroup;
						}
					} catch (Throwable e) {
						hasMasterButErr = true;
					}
					Thread.sleep(500);
				}
				if (group.getMaster() != null) {
					group.addSlaver(master);
					hasMasterButErr = false;
				}
			} while (hasMasterButErr);

			if (group.getMaster() == null) {
				/* 组中并没有设置master,默认将第一个设置为master */
				group.setMaster(group.getSlaverList().get(0));
			}
			log.info("Discovery the master or slaver server thread finished normally");
			return group;
		}
	}

	/**
	 * @desc ping(ping连接测试)
	 * @param shard
	 * @author xiaolu.zhang
	 * @date 2016年9月12日 下午10:25:01
	 */
	public static boolean ping(RedisShardInfo shard) {
		if (null == shard) {
			return false;
		}
		Jedis jedis = shard.createResource();
		return "PONG".equals(jedis.ping());
	}

	/**
	 * @desc isMaster(判断是否是主库)
	 * @param shard
	 * @author xiaolu.zhang
	 * @date 2016年9月12日 下午10:24:15
	 */
	public static boolean isMaster(RedisShardInfo shard) {
		if (null == shard) {
			return false;
		}
		Jedis jedis = shard.createResource();
		String role = redisInfo(jedis, RedisConfig.ROLE);
		return RedisConfig.ROLE_MASTER.equals(role.trim());
	}

	/**
	 * @desc redisInfo(根据关键字获取redis信息 )
	 * @param jedis
	 * @param key
	 * @author xiaolu.zhang
	 * @date 2016年9月12日 下午10:24:42
	 */
	public static String redisInfo(Jedis jedis, String key) {
		return redisInfo((BinaryJedis) jedis, key);
	}

	/**
	 * @desc redisInfo(获取redis信息)
	 * @param jedis
	 * @param key
	 * @author xiaolu.zhang
	 * @date 2016年9月12日 下午10:23:20
	 */
	public static String redisInfo(BinaryJedis jedis, String key) {
		Assert.notNull(jedis, "param jedis is null");
		Assert.notNull(key, "param key is null");
		String info = jedis.info();
		System.out.println(info);
		String[] allCfg = info.split(RedisConfig.INFO_SPLITOR);
		for (String cfg : allCfg) {
			if (StringUtils.isBlank(cfg)) {
				continue;
			}
			String[] cfgInfo = cfg.split(RedisConfig.INFO_SUB_SPLITOR);
			if (cfgInfo.length == 2 && key.equals(cfgInfo[0])) {
				return cfgInfo[1];
			}
		}
		return "";
	}

	/**
	 * @ClassName: RedisConfig
	 * @Function: 内部类静态配置
	 * @date: 2016年9月12日 下午10:23:27
	 * @author xiaolu.zhang
	 */
	public static class RedisConfig {
		public static final String INFO_SPLITOR = "\r\n";
		public static final String INFO_SUB_SPLITOR = ":";
		public static final String INFO_SUB_CHILD_SPLITOR = ",";
		public static final String ROLE = "role";
		public static final String ROLE_MASTER = "master";
		public static final String ROLE_SLAVER = "slave";

	}

}
