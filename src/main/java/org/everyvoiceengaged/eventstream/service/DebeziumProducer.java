package org.everyvoiceengaged.eventstream.service;

import org.apache.catalina.Engine;
import org.apache.catalina.startup.EngineConfig;
import org.apache.kafka.common.protocol.types.Struct;
import org.springframework.boot.context.properties.PropertyMapper.Source;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class DebeziumProducer{

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final EmbeddedEngine engine;

    public DebeziumProducer( KafkaTemplate<String, String> kafkaTemplate ){
        this.kafkaTemplate = kafkaTemplate;
        Configuration config = Configuration.create()
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


        engine = engine.create()
        .using( config )
        .notifying( this::handleEvent )
        .build();

        engine.start(); 
    }

    public void handleEvents(Source record){
        Struct value = (Struct) record.value();
        Struct after = value.getStruct("after");

        Integer id = after.getInt("id");
        String name = after.getString("name");

        String message = String.format( "id: %d, name: %s", id, name );
        kafkaTemplate.send("my-topic", message);
    }
}