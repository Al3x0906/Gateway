package org.project.gateway.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RedisConnectionTestService {

    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public RedisConnectionTestService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void testConnection() {
        try {
            redisTemplate.opsForValue().set("person", "praval");
            String  person = redisTemplate.opsForValue().get("person");
            log.info(person);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
