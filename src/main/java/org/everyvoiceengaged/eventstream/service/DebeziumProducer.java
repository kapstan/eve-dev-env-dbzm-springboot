package org.everyvoiceengaged.eventstream.service;

import io.debezium.config.Configuration;
import io.debezium.embedded.EmbeddedEngine;
import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;
import org.apache.kafka.connect.storage.FileOffsetBackingStore;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class DebeziumProducer{

    private final KafkaTemplate<String, String> kafkaTemplate;
    //private final EmbeddedEngine engine;
    private DebeziumEngine<ChangeEvent<String, String>> deezEngine;

    public DebeziumProducer( KafkaTemplate<String, String> kafkaTemplate ){
        this.kafkaTemplate = kafkaTemplate;
        Configuration config = Configuration.create()
        .with("connector.class", "io.debezium.connector.postgresql.PostgresConnector")
        .with("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore")
        .with("connector.storage.file.filename", "~/offset.dat")
        .with("offset.flush.interval.ms", 6000)
        .with("name", "my-connector")
        .with("database.hostname", "localhost")
        .with("database.port", 5432)
        .with("database.user", "postgresuser"/*"postgres"*/)
        .with("database.password", "postgrespwd"/*postgres"*/)
        .with("database.dbname", "eve_dev_db"/* "mydb" */)
        .with("table.whitelist", "eve_dev_db.mytable")
        .with("topic.prefix", "my-topic__")
        .build();

       Properties props = config.asProperties();
        
            deezEngine = DebeziumEngine.create(Json.class)
            .using(props)
            .notifying(record -> {
                System.out.println(record);
                kafkaTemplate.send("my-topic", record.toString());
            })
            .build();
        
        
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(deezEngine);
        
        
        /*engine = EmbeddedEngine.create()
            .using( config )
            .notifying( this::handleEvents )
            .build();
        
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(engine);
        System.out.print("DebeziumProducer Constructor: This puppy is running");*/
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

    public void handleEvents(SourceRecord sourceRecord){
        Struct value = (Struct) sourceRecord.value();
        Struct after = value.getStruct("after");

        Integer id = after.getInt32("id");
        String name = after.getString("name");

        String message = String.format( "id: %d, name: %s", id, name );
        kafkaTemplate.send("my-topic", message);
        System.out.print("HandledEvent");
    }
}