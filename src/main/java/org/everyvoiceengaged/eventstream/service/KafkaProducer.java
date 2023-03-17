package org.everyvoiceengaged.eventstream.service;

import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;

@Service
public class KafkaProducer{
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducer( KafkaTemplate<String, String> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage( String message ){
        kafkaTemplate.send ( "my-topic", message );
    }
}
