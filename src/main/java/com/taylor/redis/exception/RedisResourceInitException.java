package com.taylor.redis.exception;

public class RedisResourceInitException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -5821363974113367523L;

    public RedisResourceInitException(String msg, Throwable e){
        super(msg, e);
    }

}
