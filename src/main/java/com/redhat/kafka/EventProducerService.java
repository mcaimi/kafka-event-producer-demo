package com.redhat.kafka;

// import basic libraries
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
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
    protected String kafkaServers;

    @ConfigProperty(name = "vertx.producer.topic")
    protected String kafkaTopic;

    @ConfigProperty(name = "vertx.producer.topic.key.serializer")
    protected String keySerializer;

    @ConfigProperty(name = "vertx.producer.topic.value.serializer")
    protected String valueSerializer;

    @ConfigProperty(name = "vertx.producer.authenticated", defaultValue="false")
    protected boolean producerAuthenticated;

    @ConfigProperty(name = "vertx.producer.username")
    protected Optional<String> producerUsername;

    @ConfigProperty(name = "vertx.producer.password")
    protected Optional<String> producerPassword;

    @ConfigProperty(name = "vertx.security.protocol")
    protected Optional<String> securityProtocol;

    @ConfigProperty(name = "vertx.sasl.mechanism")
    protected Optional<String> saslMechanism;

    @ConfigProperty(name = "vertx.sasl.loginclass")
    protected Optional<String> loginClass;

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

        // configure SASL-SSL authentication if needed
        if (producerAuthenticated) {
          kafkaConfig.put("sasl.jaas.config", loginClass.get() + " required username=" + producerUsername.get() + " password=" + producerPassword.get() + ";");
          kafkaConfig.put("security.protocol", securityProtocol.get());
          kafkaConfig.put("sasl.mechanism", saslMechanism.get());
          kafkaConfig.put("ssl.endpoint.identification.algorithm", "https");
        }

        // build the Kafka Producer client
        kafkaProducer = KafkaProducer.create(vertx, kafkaConfig);
    }

    // send message to kafka broker
    public Future<Void> publishMessage(Event event) {
        KafkaProducerRecord<String, Event> record = KafkaProducerRecord.create(kafkaTopic, event.id.toString(), event);

        try {
            return kafkaProducer.write(record);
        } catch (Exception e) {
            throw e;
        }
    }
}
