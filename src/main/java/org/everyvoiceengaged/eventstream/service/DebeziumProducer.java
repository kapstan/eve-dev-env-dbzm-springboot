package org.everyvoiceengaged.eventstream.service;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import io.debezium.config.Configuration;
import io.debezium.data.Envelope;
import io.debezium.embedded.EmbeddedEngine;
import io.debezium.engine.DebeziumEngine;

@Service
public class DebeziumProducer{

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final EmbeddedEngine engine;

    public DebeziumProducer( KafkaTemplate<String, String> kafkaTemplate ){
        this.kafkaTemplate = kafkaTemplate;
        Configuration props = Configuration.create()
        .with("connector.class", "io.debezium.connector.postgresql.PostgresConnector")
        .with("offset.storage", "org.apache.kafka.connect.storage.FileOffsetbackingStore")
        .with("connector.storage.file.filename", "/tmp/offset.dat")
        .with("offset.flush.interval.ms", 60000)
        .with("name", "my-connector")
        .with("database.hostname.", "localhost")
        .with("database.port", 5432)
        .with("database.user", "postgres")
        .with("database.password", "postgres")
        .with("database.dbname", "mydb")
        .with("table.whitelist", "mytable")
        .build();


        engine = EmbeddedEngine.create()
        .using( props )
        .notifying( this::handleEvents )
        .build();

        engine.run(); 
    }

    public void handleEvents(SourceRecord record){
        Struct value = (Struct) record.value();
        Struct after = value.getStruct("after");

        Integer id = after.getInt32("id");
        String name = after.getString("name");

        String message = String.format( "id: %d, name: %s", id, name );
        kafkaTemplate.send("my-topic", message);
    }
}