package com.taylor.redis.common.shard;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

public class RedisMasterSlaverGroup {

	private Integer id;

	private String name;

	/***
	 * 写——分片，片段信息
	 */
	private RedisShardInfo master;

	/***
	 * 读——分片，片段信息列表
	 */
	private List<RedisShardInfo> slaverList;

	public String getName() {

		return name;
	}

	@Override
	public String toString() {
		StringBuilder value = new StringBuilder(master != null ? "{master:" + this.master.toString() + "}" : "");
		if (CollectionUtils.isNotEmpty(slaverList)) {
			for (RedisShardInfo slaver : slaverList) {
				value.append("\n\r {slavers:");
				value.append(slaver.toString());
			}
			value.append("}");
		}
		return value.toString();
	}

	/**
	 * @return the master
	 */
	public RedisShardInfo getMaster() {
		return master;
	}

	/**
	 * @param master
	 *            the master to set
	 */
	public void setMaster(RedisShardInfo master) {
		this.master = master;
	}

	/**
	 * @return the slaverList
	 */
	public List<RedisShardInfo> getSlaverList() {
		return slaverList;
	}

	/**
	 * @param slaverList
	 *            the slaverList to set
	 */
	public void setSlaverList(List<RedisShardInfo> slaverList) {
		this.slaverList = slaverList;
	}

	/**
	 * @param slaverList
	 *            the slaver to set
	 */
	public void addSlaver(RedisShardInfo slaver) {
		if (null == this.slaverList) {
			this.slaverList = new ArrayList<RedisShardInfo>();
		}
		this.slaverList.add(slaver);
	}

	public Integer getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

}
