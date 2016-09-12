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

		public RedisMasterSlaverGroup call() throws Exception {
			log.info("Discovery the master or slaver server thread start....");
			if (null == shardGroup) {
				return null;
			}
			// all shard info
			List<RedisShardInfo> shards = new ArrayList<RedisShardInfo>();
			// cur master, is null when initialize
			final RedisShardInfo master = shardGroup.getMaster();
			// true: master exists but cannot connect
			boolean hasMasterButErr = false;
			System.out.println(master);
			if (master != null) {
				try {
					// master available , return
					if (isMaster(master))
						return shardGroup;
					else {
						// not a master but can server add to slaver list
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
				// 1.do find a master from slaver list
				if (CollectionUtils.isNotEmpty(shards)) {
					for (RedisShardInfo shard : shards) {
						try {
							if (isMaster(shard)) {
								if (!hasMasterButErr && group.getMaster() != null) {
									// has two differ master
									if (!shard.equals(group.getMaster())) {
										throw new RedisMultiMasterShardException("Exists multi master server in shard group : " + shardGroup.getId());
									}
									// slaver is same to master
									group.addSlaver(shard);
								}
								group.setMaster(shard);
							} else {
								group.addSlaver(shard);
							}
						} catch (Throwable e) {
							log.error(new RedisShardConnectException("Failed get role info of server,will put as a slaver" + shard.toString(), e), e);
							group.addSlaver(shard);
						}
					}
				}

				// 2. hasMasterButErr == true, check master back to nomally,if
				// not wait 0.5s do this job again
				// mostly happend when master server became unavailable,and have
				// not a slaver server switch to a master
				if (hasMasterButErr && group.getMaster() == null) {
					try {
						if (isMaster(master)) {
							hasMasterButErr = false;
							// MASTER resume to normal,return;
							return shardGroup;
						}
					} catch (Throwable e) {
						hasMasterButErr = true;
					}
					// 3. wati for switch slaver to master successly
					Thread.sleep(500);
				}

				if (group.getMaster() != null) {
					// get a master from slaver list, add master as slaver,and
					// waiting for serving normally
					group.addSlaver(master);

					hasMasterButErr = false;
				}

			} while (hasMasterButErr);

			// mostly happend when initialize without master server or it is
			// unavailable
			if (group.getMaster() == null) {
				// throw new RedisMasterShardNotExistsException("group [" +
				// groupId + "] has not a master shard");
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
