package org.everyvoiceengaged.eventstream;

import org.everyvoiceengaged.eventstream.config.KafkaConfig;
import org.everyvoiceengaged.eventstream.service.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication

public class DemoApp{

    @Autowired
    private static KafkaConfig config = new KafkaConfig();

    public static void main(String[] args){
        SpringApplication.run(DemoApp.class, args);
    }

    @Bean
    public CommandLineRunner runner(KafkaProducer producer){
        return args -> {
            producer.sendMessage("Testing 123");
            producer.sendMessage("Hi EVE!");
            for (int i = 1; i < 50; i++){
                producer.sendMessage("Test message number" + i);
            }
        };
    }

    /*@Bean 
    public NewTopic topic() {
        return TopicBuilder.name("my-topic").partitions(1).replicas(1).build();
    }*/
}