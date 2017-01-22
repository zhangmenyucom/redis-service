package com.taylor.redis.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface RedisCacheClean {
    /**
     * key值
     *
     * @return
     */
    public  String[] key();

	/**
	 * 是否批量
	 * 
	 * @return
	 */
	public boolean isBatch() default false;
}
