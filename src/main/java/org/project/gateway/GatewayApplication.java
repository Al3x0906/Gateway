package org.project.gateway;

import org.project.gateway.service.RedisConnectionTestService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args).getBean(RedisConnectionTestService.class).testConnection();
    }
}
