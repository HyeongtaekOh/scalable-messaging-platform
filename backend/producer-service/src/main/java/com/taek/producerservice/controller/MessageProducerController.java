package com.taek.producerservice.controller;

import com.taek.producerservice.domain.Message;
import com.taek.producerservice.service.MessageProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageProducerController {

    private final MessageProducerService producerService;

    @PostMapping
    public Mono<ResponseEntity<String>> sendMessage(@RequestBody Message message) {
        message.setTimestamp(System.currentTimeMillis());
        return producerService.sendMessage(message)
            .map(success -> success ? ResponseEntity.ok("Message sent successfully")
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send message"));
    }
}
