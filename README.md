# EVE Sample Dev Environment

This is a test development environment, complete with a SpringBoot project, and docker-compose config for Kafka, Zookeeper, PostgresSQL, and the Debezium PostGres connector.

---

### Dependencies

- Docker Desktop
- JDK v11
- Kotlin v1.6

---

### How do I get this going?
1. Clone the repo
2. At a command-line in your repo folder, run `docker-compose up` which will stand-up 4 containers: Kafka, Zookeeper, Postgres, and the Debezium Postgres Connector
    - Scripts inside ./init-scripts will be executed inside the postgres container at /docker-entrypoint-initdb.d
3. Run the project by either running "maven spring-boot:run" at a command line or by choosing the Maven:Spring-Boot: Run task inside your IDE of choice.
    - This registers a Kafka Consumer at http://localhost:8080
4. You'll now need to "add" the connector which is going to register a Kafka Producer which will shunt row change messages from the Postgres database to the Kafka cluster. Do this by visiting http://localhost:8080/connectors/add.
    - You can alter connection parameters in the connector configuration by opening src/main/resources/connector-config.json

## Test
1. At a CLI, run the following command:
```
docker exec -it CONTAINER_ID_FOR_POSTGRES_CONTAINER sh
```
2. Log into Postgres with the following command and credentials (these are also set in the docker-compose as well as the connector config)
```
psql -U postgresuser -d eve_dev_db
```
3. Insert some new records into the database.
    - Note, the sender_id and rcvr_id columns datatype in the database schema is UUID, which means to insert records, you'll need to generate valid UUIDs. You can do this at a command prompt like so (on MacOS, Linux, or WSL2):
    ```
    uuidgen | tr -d - | tr -d '\n' | tr '[:upper:]' '[:lower:]' | pbcopy && pbpaste && echo
    ```
    That command will dump out a valid UUID which you can use to insert some new records.
    - Sorry about that choice for the column datatype, seemed like a good idea at the time x-(
4. The changes you made to the table will be reflected in the Spring Boot server logs (within your IDE's log panel or, if you started the project from CLI using the ```mvn``` executable, you'll see logs tailed in that terminal session).


