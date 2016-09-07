package com.taylor.redis.common.client;

import lombok.Data;

@Data
public class RedisSimpleClientInfo {
	private String host;
    private int port;
    private String instanceid;
    private String password;
}
