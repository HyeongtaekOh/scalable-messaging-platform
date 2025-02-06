package com.taek.producerservice.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.taek.producerservice.container.RedisTestContainer;
import com.taek.producerservice.domain.Message;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Import({RedisTestContainer.class})
public class MessageProducerServiceTest {

    @Autowired
    private MessageProducerService producerService;

    @Autowired
    private ReactiveRedisTemplate<String, String> redisTemplate;

    private static final String STREAM_KEY = "chat:messages";

    @Test
    void 메시지를_정상적으로_발행한다() {

        // given
        Message message = new Message("123", "user1", "Hello!", System.currentTimeMillis());

        // when
        Mono<Boolean> result = producerService.sendMessage(message);

        // then
        StepVerifier.create(result)
            .expectNext(true)
            .verifyComplete();

        StepVerifier.create(redisTemplate.opsForStream().range(STREAM_KEY, Range.unbounded()).collectList())
            .assertNext(records -> {
                assertThat(records).isNotEmpty();
                assertThat(records.get(0).getValue()).containsEntry("roomId", "123");
                assertThat(records.get(0).getValue()).containsEntry("message", "Hello!");
            })
            .verifyComplete();
    }
}