package com.taek.producerservice.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.taek.producerservice.container.RedisTestContainer;
import com.taek.producerservice.domain.Message;
import com.taek.producerservice.service.MessageProducerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest(MessageProducerController.class)
@Import({MessageProducerService.class, RedisTestContainer.class})
class MessageProducerControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private MessageProducerService producerService;

    @Test
    void 메시지_생성_API_정상_동작() {
        // given
        Message message = new Message("123", "user1", "Hello ControllerTest!", System.currentTimeMillis());
        when(producerService.sendMessage(any(Message.class)))
            .thenReturn(Mono.just(Boolean.TRUE));

        // when % then
        webTestClient.post()
            .uri("/messages")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(message)
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class).isEqualTo("Message sent successfully");
    }

    @Test
    void 메시지_생성_API_정상_실패() {
        // given
        Message message = new Message("123", "user1", "Hello ControllerTest!", System.currentTimeMillis());
        when(producerService.sendMessage(any(Message.class)))
            .thenReturn(Mono.just(Boolean.FALSE));

        // when % then
        webTestClient.post()
            .uri("/messages")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(message)
            .exchange()
            .expectStatus().is5xxServerError()
            .expectBody(String.class).isEqualTo("Failed to send message");
    }
}