package com.redhat.kafka;

import java.util.HashMap;
import java.util.Map;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

// import kafka for junit resources
import net.mguenther.kafka.junit.EmbeddedKafkaCluster;
import net.mguenther.kafka.junit.TopicConfig;

import static net.mguenther.kafka.junit.EmbeddedKafkaCluster.provisionWith;
import static net.mguenther.kafka.junit.EmbeddedKafkaClusterConfig.defaultClusterConfig;

// declare a new resource LM that will handle the embedded kafka deployment
public class EmbeddedKafkaResource implements QuarkusTestResourceLifecycleManager {

    // embedded kafka cluster
    private EmbeddedKafkaCluster testCluster;

    @Override
    public Map<String, String> start() {
        Map<String, String> kafkaProperties = new HashMap<String, String>();

        // start the embedded kafka cluster
        testCluster = provisionWith(defaultClusterConfig());
        testCluster.start();

        // create test topic
        testCluster.createTopic(TopicConfig.withName("event-input-topic"));

        // set broker list as application property for test profile
        kafkaProperties.put("test.kafka.bootstrap.servers.list", testCluster.getBrokerList());

        // return properties
        return kafkaProperties;
    }

    @Override
    public void stop() {
        // tear down the embedded kafka cluster
        testCluster.stop();
    }
}