package com.taek.producerservice.service;

import com.taek.producerservice.domain.Message;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Log4j2
@Service
@RequiredArgsConstructor
public class MessageProducerService {

    private final ReactiveRedisTemplate<String, String> redisTemplate;

    private static final String STREAM_KEY = "chat:messages";

    public Mono<Boolean> sendMessage(Message message) {
        MapRecord<String, String, String> record = StreamRecords.newRecord()
            .ofMap(Map.of(
                "roomId", message.getRoomId(),
                "sender", message.getSender(),
                "message", message.getMessage(),
                "timestamp", String.valueOf(message.getTimestamp())
            ))
            .withStreamKey(STREAM_KEY);

        return redisTemplate.opsForStream().add(record)
            .doOnSuccess(id -> log.info("Message sent with ID: {}", id))
            .map(Objects::nonNull);
    }
}
