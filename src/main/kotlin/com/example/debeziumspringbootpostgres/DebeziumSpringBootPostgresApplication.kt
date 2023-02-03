package com.example.debeziumspringbootpostgres

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DebeziumSpringBootPostgresApplication

fun main(args: Array<String>) {
  runApplication<DebeziumSpringBootPostgresApplication>(*args)
}
