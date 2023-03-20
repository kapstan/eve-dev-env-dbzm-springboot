package org.everyvoiceengaged.eventstream;

import org.apache.kafka.clients.admin.NewTopic;
import org.everyvoiceengaged.eventstream.service.KafkaProducer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;

@SpringBootApplication
public class DemoApp{
    public static void main(String[] args){
        SpringApplication.run(DemoApp.class, args);
    }

    @Bean
    public CommandLineRunner runner(KafkaProducer producer){
        return args -> {
            producer.sendMessage("Hi EVE!");
        };
    }

    /*@Bean 
    public NewTopic topic() {
        return TopicBuilder.name("my-topic").partitions(1).replicas(1).build();
    }*/
}