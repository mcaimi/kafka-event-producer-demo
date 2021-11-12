package com.redhat.kafka;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

import java.util.Date;
import java.util.UUID;;

@QuarkusTest
public class EventProducerResourceTest {

    @Test
    public void testMicroprofilePublishEndpoint() {

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
}