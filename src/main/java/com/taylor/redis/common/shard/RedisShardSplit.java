package com.taylor.redis.common.shard;

import java.util.Collection;
import java.util.List;

public interface RedisShardSplit<R, T> {

    /**
     * 初始构建 资源列表
     * 
     * @param shards
     * @return
     */
    public void initResources(List<T> shards);

    /**
     * 重新初始化shards中的资源，若资源列表中不存在，则抛错
     * 
     * @param shards
     * @return
     */
    public void reBuildResources(List<T> shards);

    /**
     * 关闭连接池
     */
    public void destoryAllResources();

    /**
     * 根据key获取分片信息
     * 
     * @param key
     * @return
     */
    public T getShardInfo(byte[] key);

    /**
     * 根据key获取分片
     * 
     * @param key
     * @return
     */
    public R getShardPool(byte[] key);

    /**
     * 根据key获取分片
     * 
     * @param key
     * @return
     */
    public R getShardPool(String key);

    /**
     * GET ALL CONFIG
     * 
     * @see https://github.com/xetorthio/jedis/blob/master/src/main/java/redis/clients/util/Sharded.java
     * @return
     */
    public Collection<T> getAllShardInfo();

}
