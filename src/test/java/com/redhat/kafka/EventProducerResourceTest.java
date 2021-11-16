package com.redhat.kafka;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

import java.util.Date;
import java.util.UUID;

@QuarkusTest
@QuarkusTestResource(EmbeddedKafkaResource.class)
public class EventProducerResourceTest {

    @Test
    public void testMicroprofileEndpoint() {

        String requestBody = "{ \"id\": \"ID\", \"message\": \"MSG\", \"severity\": SEV, \"event_timestamp\": TS }";
        Long timestamp = new Date().getTime();
        System.out.println(requestBody
                .replace("ID", UUID.randomUUID().toString())
                .replace("MSG", "TestMessageJUnit")
                .replace("SEV", "1")
                .replace("TS", timestamp.toString()));
        
        given()
          .when()
          .body(requestBody
                .replace("ID", UUID.randomUUID().toString())
                .replace("MSG", "TestMessageJUnit")
                .replace("SEV", "1")
                .replace("TS", timestamp.toString())
                )
          .contentType("application/json")
          .post("/producer/post")
          .then()
             .statusCode(200)
             .header("APIState", equalTo("PUBLISH_OK"));
    }

    @Test
    public void testMicroprofileWrongPayload(){
        // timestamp is missing, this is an error
        String requestBody = "{ \"id\": \"ID\", \"message\": \"MSG\", \"severity\": SEV }";

        System.out.println(requestBody
                .replace("ID", UUID.randomUUID().toString())
                .replace("MSG", "TestMessageJUnit")
                .replace("SEV", "1")
                );
        
        given()
          .when()
          .body(requestBody
                .replace("ID", UUID.randomUUID().toString())
                .replace("MSG", "TestMessageJUnit")
                .replace("SEV", "1")
                )
          .contentType("application/json")
          .post("/producer/post")
          .then()
             .statusCode(400)
             .header("APIState", equalTo("Missing timestamp from message payload"));
    }
}