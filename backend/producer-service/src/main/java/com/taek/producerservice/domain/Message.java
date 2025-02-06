package com.taek.producerservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private String roomId;
    private String sender;
    private String message;
    private Long timestamp;
}
