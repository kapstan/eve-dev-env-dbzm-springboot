package com.example.debeziumspringbootpostgres.controller

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import java.nio.file.Files

@RestController
@RequestMapping("/connectors")
class ConnectorConfigController() {

  val restTemplate: RestTemplate = RestTemplateBuilder().build();
  val connectorBaseUrl = "http://localhost:8083/connectors";
  val logger: Logger = LoggerFactory.getLogger(ConnectorConfigController::class.java);

  @GetMapping("")
  fun getConnectors(): ResponseEntity<Any> {
    val response =
      restTemplate.getForEntity("$connectorBaseUrl?expand=info&expand=status", Object::class.java);
    return ResponseEntity(response.body, response.statusCode);
  }

  @GetMapping("/add")
  fun addConnector(): ResponseEntity<Any> {
    val file = ClassPathResource("connector-config.json").file;
    val text = String(Files.readAllBytes(file.toPath()));
    val headers = HttpHeaders()
    headers.contentType = MediaType.APPLICATION_JSON
    val requestEntity = HttpEntity(text, headers)
    val response = restTemplate.postForEntity(connectorBaseUrl, requestEntity, Object::class.java);
    return ResponseEntity(response, response.statusCode);
  }

  @GetMapping("/delete")
  fun deleteConnector(): ResponseEntity<Any> {
    val headers = HttpHeaders()
    headers.contentType = MediaType.APPLICATION_JSON
    val requestEntity = HttpEntity(null, headers)
    val response = restTemplate.delete(
      "$connectorBaseUrl/example-debezium-postgres-connector",
      requestEntity,
      Object::class.java
    );
    return ResponseEntity(response, HttpStatus.OK);
  }
}