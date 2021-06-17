package com.security.demo.configuration.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.FlushMode;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

@Configuration
public class WebSessionConfiguration extends RedisHttpSessionConfiguration {

    public static String redisNamespace = "/supervision";
    @Value("${spring.session.redis.timeout:14400}")
    private int maxInactiveIntervalInSeconds;
    @Value("${spring.session.redis.flushMode:immediate}")
    private String flushMode;

    /**
     * 兼容1.X session的前缀，由于序列化问题，放弃兼容，改为其他方案、
     */
    static final String DEFAULT_SPRING_SESSION_REDIS_PREFIX = "spring:session:";

    @Bean
    public RedisIndexedSessionRepository sessionRepository() {
        RedisIndexedSessionRepository repository = super.sessionRepository();
        repository.setFlushMode(FlushMode.IMMEDIATE);
        repository.setDefaultMaxInactiveInterval(maxInactiveIntervalInSeconds);
        repository.setRedisKeyNamespace(DEFAULT_SPRING_SESSION_REDIS_PREFIX + redisNamespace);
        return repository;
    }

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
        cookieSerializer.setUseBase64Encoding(false);
        return cookieSerializer;
    }
}