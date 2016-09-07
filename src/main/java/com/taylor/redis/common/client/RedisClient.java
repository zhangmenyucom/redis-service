/**
 * 
 */
package com.taylor.redis.common.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.annotation.PostConstruct;

import lombok.extern.log4j.Log4j;
import redis.clients.jedis.Jedis;
import redis.clients.util.SafeEncoder;

/**
 * @author HaydenWang
 *
 */
@Log4j
public class RedisClient {

	private Jedis jedis;
	private String host;
	private int port;
	private String instanceId;

	public String getInstanceId() {
		return instanceId;
	}

	private String password;

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@PostConstruct
	public void init() {
		jedis = new Jedis(host, port, 10000);
		if ((password != null) && (password.trim().length() > 0)) {
			jedis.auth(password);
		}
	}

	public void set(String key, String value) {
		jedis.set(key, value);
	}

	public void set(String key, Object value) {
		byte[] byteValue = serialize(value);
		jedis.set(SafeEncoder.encode(key), byteValue);
	}

	public Object getObject(String key) {
		byte[] result = jedis.get(SafeEncoder.encode(key));
		Object object = deserialize(result);
		return object;
	}

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

	public String get(String key) {
		byte[] result = jedis.get(SafeEncoder.encode(key));
		return new String(result);
	}
}
