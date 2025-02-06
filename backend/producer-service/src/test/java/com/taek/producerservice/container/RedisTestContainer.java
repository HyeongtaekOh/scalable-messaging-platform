package com.taek.producerservice.container;

import com.redis.testcontainers.RedisContainer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@TestConfiguration
@Testcontainers
public class RedisTestContainer {


    private static final Integer PORT = 6379;
    private static final String PASSWORD = "password";


    @Container
    public GenericContainer<?> REDIS_CONTAINER =
        new RedisContainer("redis:6-alpine")
            .withExposedPorts(PORT)
            .withCommand("redis-server", "--requirepass", PASSWORD);
    {
        REDIS_CONTAINER.start();
    }

    @Bean @Primary
    public ReactiveRedisConnectionFactory testRedisConnectionFactory() {
        String host = REDIS_CONTAINER.getHost();
        Integer mappedPort = REDIS_CONTAINER.getMappedPort(PORT);
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(host, mappedPort);
        configuration.setPassword(PASSWORD);

        return new LettuceConnectionFactory(configuration);
    }

    @Bean @Primary
    public ReactiveRedisTemplate<String, String> reactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {
        RedisSerializationContext<String, String> context =
            RedisSerializationContext.<String, String>newSerializationContext(RedisSerializer.string())
                .value(RedisSerializer.string())
                .build();
        return new ReactiveRedisTemplate<>(factory, context);
    }
}
