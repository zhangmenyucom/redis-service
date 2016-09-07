package com.taylor.redis.exception;

public class RedisMultiMasterShardException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -779037460315500943L;

    public RedisMultiMasterShardException(String msg, Throwable e){
        super(msg, e);
    }

    public RedisMultiMasterShardException(String msg){
        super(msg);
    }
}
