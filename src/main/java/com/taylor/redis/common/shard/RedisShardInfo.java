package com.taylor.redis.common.shard;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Protocol;

/**
 * @ClassName: RedisShardInfo
 * @Function: Redis客户端信息类
 * @date: 2016年9月13日 下午8:09:26
 * @author xiaolu.zhang
 */
public class RedisShardInfo {

	/* 读取redis一条命令响应超时时间 默认2秒 */
	private Integer timeout = Protocol.DEFAULT_TIMEOUT;

	/* redis服务ip地址 */
	private String host;

	/* 端口号 */
	private Integer port;

	/* 密码 */
	private String password = null;

	/* 实例id */
	private String instanceid;

	/* 组id */
	private Integer groupId;

	/* 创建一个redis客户端实例 */
	public Jedis createResource() {
		Jedis client = new Jedis(host, port, timeout);
		if ((password != null) && (password.trim().length() > 0)) {
			client.auth(password);
		}
		return client;
	}

	@Override
	public String toString() {
		return "[groupId=" + groupId + "],[host=" + host + "],[port=" + port + "]";
	}

	/**
	 * @return the timeout
	 */
	public Integer getTimeout() {
		return timeout;
	}

	/**
	 * @param timeout
	 */
	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the port
	 */
	public Integer getPort() {
		return port;
	}

	/**
	 * @param port
	 */
	public void setPort(Integer port) {
		this.port = port;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the groupId
	 */
	public Integer getGroupId() {
		return groupId;
	}

	/**
	 * @param groupId
	 */
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public String getInstanceid() {
		return instanceid;
	}

	public void setInstanceid(String instanceid) {
		this.instanceid = instanceid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((port == null) ? 0 : port.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RedisShardInfo other = (RedisShardInfo) obj;
		if (groupId == null) {
			if (other.groupId != null) {
				return false;
			}
		} else if (!groupId.equals(other.groupId)) {
			return false;
		}
		if (host == null) {
			if (other.host != null) {
				return false;
			}
		} else if (!host.equals(other.host)) {
			return false;
		}
		if (password == null) {
			if (other.password != null) {
				return false;
			}
		} else if (!password.equals(other.password)) {
			return false;
		}
		if (port == null) {
			if (other.port != null) {
				return false;
			}
		} else if (!port.equals(other.port)) {
			return false;
		}
		return true;
	}

}
