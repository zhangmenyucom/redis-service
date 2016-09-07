package com.taylor.redis.exception;

public class RedisMasterShardNotExistsException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -3976173887842551259L;

    public RedisMasterShardNotExistsException(String message){
        super(message);
    }

    public RedisMasterShardNotExistsException(String message, Throwable e){
        super(message, e);
    }

}
