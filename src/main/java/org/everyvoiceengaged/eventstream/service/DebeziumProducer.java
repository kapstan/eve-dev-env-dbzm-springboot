package org.everyvoiceengaged.eventstream.service;

import java.util.function.Consumer;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;


@Service
public class DebeziumProducer{

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final EmbeddedEngine engine;
    //private final DebeziumEngine<ChangeEvent<String, Struct>> deezEngine;

    public DebeziumProducer( KafkaTemplate<String, String> kafkaTemplate ){
        this.kafkaTemplate = kafkaTemplate;
        Configuration config = Configuration.create()
        .with("connector.class", "io.debezium.connector.postgresql.PostgresConnector")
        .with("offset.storage", "org.apache.kafka.connect.storage.FileOffsetbackingStore")
        .with("connector.storage.file.filename", "/tmp/offset.dat")
        .with("offset.flush.interval.ms", 60000)
        .with("name", "my-connector")
        .with("database.hostname", "localhost")
        .with("database.port", 5432)
        .with("database.user", "postgres")
        .with("database.password", "postgres")
        .with("database.dbname", "mydb")
        .with("table.whitelist", "mytable")
        .build();

       /*  Properties props = config.asProperties();
        try (
            deezEngine = DebeziumEngine.create(Json.class)
            .using(props)
            .notifying(this::handleEvent)
            .build();
        )
        {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(deezEngine);
        }*/
        
        engine = EmbeddedEngine.create()
            .using( config )
            .notifying( this::handleEvents )
            .build();
        
        


        engine.run();
    }

    /*public  Consumer<ChangeEvent<Struct, Struct>> handleEvent(ChangeEvent<String, String> record){
        Struct value = (Struct) record.value();
        Struct after = value.getStruct("after");

        Integer id = after.getInt32("id");
        String name = after.getString("name");

        String message = String.format( "id: %d, name: %s", id, name );
        kafkaTemplate.send("my-topic", message);
        return consumer -> {
            kafkaTemplate.send("my-topic", message);
        };
    }*/

    public void handleEvents(SourceRecord record){
        Struct value = (Struct) record.value();
        Struct after = value.getStruct("after");

        Integer id = after.getInt32("id");
        String name = after.getString("name");

        String message = String.format( "id: %d, name: %s", id, name );
        kafkaTemplate.send("my-topic", message);
    }
}