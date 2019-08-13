package com.slabs.exchange.common.config.redis;

import com.slabs.exchange.util.StringUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 类RedisConfig的功能描述:
 * Redis配置
 * @auther hxy
 * @date 2017-11-15 21:49:31
 */
@Configuration
@EnableAutoConfiguration
@Slf4j
@ConfigurationProperties(prefix = "spring.redis")
@Data
public class RedisConfig {

    private String host;
    private int port;
    private String password;
    private int timeout;

    @Bean
    public JedisPoolConfig getRedisConfig(){
        JedisPoolConfig config = new JedisPoolConfig();
        return config;
    }

    @Bean
    public JedisPool getJedisPool(){
        JedisPoolConfig config = getRedisConfig();
        String pwd = StringUtil.isBlank(password) ? null : password;
        JedisPool pool = new JedisPool(config,host,port,timeout,pwd);
        log.info("init JredisPool ...");
        return pool;
    }

}
