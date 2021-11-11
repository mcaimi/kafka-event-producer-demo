package com.redhat.kafka;

// import basic libraries
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.eclipse.microprofile.config.inject.ConfigProperty;

// import event descriptor class
import com.redhat.kafka.entities.Event;

// import vertx and vertx kafka producer libraries
import io.vertx.core.Vertx;
import io.vertx.core.Future;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.KafkaProducerRecord;

// kafka client singleton service class
// this class will asynchronously handle message production
@Singleton
public class EventProducerService {
    // read properties from configuration file
    // as Vertx does not do it automatically via CDI
    @ConfigProperty(name = "kafka.bootstrap.servers")
    private String kafkaServers;

    @ConfigProperty(name = "kafka.producer.topic")
    private String kafkaTopic;

    @ConfigProperty(name = "kafka.producer.topic.key.serializer")
    private String keySerializer;

    @ConfigProperty(name = "kafka.producer.topic.value.serializer")
    private String valueSerializer;

    // inject Vertx
    @Inject
    Vertx vertx;

    // kafka producer object
    private KafkaProducer<String, Event> kafkaProducer;

    // initialize kafka client
    @PostConstruct
    void initialize() {
        Map<String, String> kafkaConfig = new HashMap<>();

        // fill configuration information inside the Kafka Parameters map
        kafkaConfig.put("bootstrap.servers", kafkaServers);
        kafkaConfig.put("key.serializer", keySerializer);
        kafkaConfig.put("value.serializer", valueSerializer);

        // build the Kafka Producer client
        kafkaProducer = KafkaProducer.create(vertx, kafkaConfig);
    }

    // send message to kafka broker
    public Future<Void> publishMessage(Event event) {
        KafkaProducerRecord<String, Event> record = KafkaProducerRecord.create(kafkaTopic, event.id.toString(), event);
        System.out.println(record);
        try {
            return kafkaProducer.write(record);
        } catch (Exception e) {
            throw e;
        }
    }
}
