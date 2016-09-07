package com.taylor.redis.service;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public interface RedisClientFactory extends FactoryBean<Object>, InitializingBean, DisposableBean {
}
