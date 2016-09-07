package com.taylor.redis.common.client;

import redis.clients.jedis.Jedis;

public interface JedisCallBack<T> {

    public T doBiz(Jedis j);

    public String getOperationName();

}
