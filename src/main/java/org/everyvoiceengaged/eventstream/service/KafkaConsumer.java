package org.everyvoiceengaged.eventstream.service;

import org.springframework.stereotype.Service;
import org.springframework.kafka.annotation.KafkaListener;

@Service
public class KafkaConsumer{

    @KafkaListener(topics = "mytopic")
    public void receiveMessage ( String message ){
        System.out.println( "Received message: " + message);
    }
}
