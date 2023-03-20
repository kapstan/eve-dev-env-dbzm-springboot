package org.everyvoiceengaged.eventstream.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer{

    @KafkaListener(topics = "my-topic")
    public void receiveMessage ( String message, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic){
        String composedMessage = "Topic: \"" + topic + "\" Message: \"" + message + "\"";
        System.out.println(composedMessage);
    }
}
