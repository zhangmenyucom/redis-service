package com.taylor.redis.common.shard;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.taylor.redis.common.client.RedisSimpleClientInfo;

import lombok.extern.log4j.Log4j2;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Protocol;

@Log4j2
public class RedisSimplePool {

	private JedisPool jedisPool;

	public RedisSimplePool(RedisSimpleClientInfo redisClient, GenericObjectPoolConfig poolConfig) {
		if (StringUtils.isBlank(redisClient.getPassword())) {
			jedisPool = new JedisPool(poolConfig, redisClient.getHost(), redisClient.getPort(), Protocol.DEFAULT_TIMEOUT);
		} else {
			jedisPool = new JedisPool(poolConfig, redisClient.getHost(), redisClient.getPort(), Protocol.DEFAULT_TIMEOUT, redisClient.getPassword());
		}
	}

	public Jedis getResource() {
		return jedisPool.getResource();
	}

	public void returnResource(Jedis jedis) {
		jedisPool.returnResource(jedis);
	}

	public void destoryAllResources() {
		try {
			jedisPool.destroy();
		} catch (Exception e) {
			log.error("redisPool destory error:", e);
		}
	}
}
