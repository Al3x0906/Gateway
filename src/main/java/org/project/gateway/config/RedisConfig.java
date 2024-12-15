package org.project.gateway.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Slf4j
@Configuration
public class RedisConfig {

    @Primary
    @Bean
    public <T, E> RedisTemplate<T, E> getRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<T, E> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        return redisTemplate;
    }

}
