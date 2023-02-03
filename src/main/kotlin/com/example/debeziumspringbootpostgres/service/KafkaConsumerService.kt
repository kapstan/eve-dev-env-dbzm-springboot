package com.example.debeziumspringbootpostgres.service

import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import java.io.IOException

@Service
@EnableKafka
class KafkaConsumerService {
  private val logger = LoggerFactory.getLogger(KafkaConsumerService::class.java)

  @KafkaListener(topics = ["example.public.messages"], groupId = "2") @Throws(
    IOException::class
  ) fun consume(message: String?) {
    logger.info(String.format("*** Consumed message -> %s", message))
  }
}