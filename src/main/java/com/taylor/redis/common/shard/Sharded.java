package com.taylor.redis.common.shard;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import com.taylor.redis.common.client.JedisCallBack;
import com.taylor.redis.exception.RedisShardConnectException;

import lombok.extern.log4j.Log4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.util.SafeEncoder;

@Log4j
public abstract class Sharded {

	protected RedisShardSplit<JedisPool, RedisMasterSlaverGroup> splitor;

	public Sharded() {
	}

	public Sharded(RedisShardSplit<JedisPool, RedisMasterSlaverGroup> splitor) {
		this.splitor = splitor;
	}

	public JedisPool getShardPool(byte[] key) {
		return splitor.getShardPool(key);
	}

	public JedisPool getShardPool(String key) {
		return splitor.getShardPool(key);
	}

	public <E> E doOperation(String key, JedisCallBack<E> callback) {

		return this.doOperation(SafeEncoder.encode(key), callback);
	}

	public <E> E doOperation(byte[] key, JedisCallBack<E> callback) {

		JedisPool pool = null;
		Jedis j = null;

		try {
			pool = getShardPool(key);
			j = pool.getResource();
			E rs = callback.doBiz(j);
			return rs;
		} catch (JedisConnectionException e) {
			// 1. catch jedis connections exception,do....
			// 1),probe the servers,and reprobe the master may switched from
			// slaver when the master server unavaliable
			splitor.reBuildResources(Arrays.asList(splitor.getShardInfo(key)));
			// 2),return resouces
			if (null != pool && null != j) {
				pool.returnBrokenResource(j);
			}
			log.error("Do operation failed for key :=" + key, e);
			// 3),throw the exception
			throw new RedisShardConnectException("Faild when execute operation  " + callback.getOperationName(), e);

		} catch (Throwable e2) {
			// 2.catch other,do...
			if (e2.getMessage() != null && e2.getMessage().contains("READONLY")) {
				// 0),probe the servers,and reprobe the master may switched from
				// slaver when the master server unavaliable
				splitor.reBuildResources(Arrays.asList(splitor.getShardInfo(key)));
			}

			// 1),return resouces
			if (null != pool && null != j) {
				pool.returnBrokenResource(j);
			}
			log.error("Do operation failed for key :=" + key, e2);
			// 2),throw the exception
			throw new RuntimeException("Do operation failed for key :=" + key, e2);
		} finally {
			if (null != pool && null != j)
				pool.returnResource(j);
		}

	}

	/**
	 * Get the bytes representing the given serialized object.
	 * 
	 * @param o
	 * @return
	 */
	protected byte[] serialize(Object o) {
		if (o == null) {
			return new byte[0];
		}
		byte[] rv = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream os = new ObjectOutputStream(bos);
			os.writeObject(o);
			os.close();
			bos.close();
			rv = bos.toByteArray();
		} catch (IOException e) {
			throw new IllegalArgumentException("Non-serializable object", e);
		}
		return rv;
	}

	/**
	 * Get the object represented by the given serialized bytes.
	 * 
	 * @param in
	 * @return
	 */
	protected Object deserialize(byte[] in) {
		Object rv = null;
		try {
			if (in != null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(in);
				ObjectInputStream is = new ObjectInputStream(bis);
				rv = is.readObject();
				is.close();
				bis.close();
			}
		} catch (IOException e) {
			log.warn("Caught IOException decoding %d bytes of data", e);
		} catch (ClassNotFoundException e) {
			log.warn("Caught CNFE decoding %d bytes of data", e);
		}
		return rv;
	}

	public void setSplitor(RedisShardSplit<JedisPool, RedisMasterSlaverGroup> splitor) {
		this.splitor = splitor;
	}

}
