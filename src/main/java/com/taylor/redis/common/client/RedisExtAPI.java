package com.taylor.redis.common.client;

public interface RedisExtAPI {

    public String set(String key, Object value);

    public Object getObject(String key);

}
