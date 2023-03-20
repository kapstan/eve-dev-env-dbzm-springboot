package org.everyvoiceengaged.eventstream.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer{

    @KafkaListener(topics = "my-topic")
    public void receiveMessage ( String message ){
        System.out.println( "Received message: " + message);
    }
}
