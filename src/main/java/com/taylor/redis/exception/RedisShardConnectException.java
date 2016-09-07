package com.taylor.redis.exception;

public class RedisShardConnectException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 163921566144673193L;

    public RedisShardConnectException(String message, Throwable e){
        super(message, e);
    }

    public RedisShardConnectException(String message){
        super(message);
    }
}
