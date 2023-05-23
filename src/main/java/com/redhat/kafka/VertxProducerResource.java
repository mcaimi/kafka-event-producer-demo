package com.redhat.kafka;

// import common resources
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

// import event descriptor class
import com.redhat.kafka.entities.Event;

@Path("/vertx")
public class VertxProducerResource {

    // inject Vertx Kafka Client Service
    @Inject
    EventProducerService vertxProducer;

    // Wire route to the kafka client singleton
    // use vertx to send messages to kafka topics
    @POST
    @Path("/post")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response vertxPost(Event event) {
        
        // refuse events without timestamp
        if (event.event_timestamp == null) {
            return Response.status(Status.BAD_REQUEST).header("APIState", "Missing timestamp from message payload").build();
        }
        
        try {
            // set ingestion timestamp
            event.setIngestionTimestamp(); // now
            // send message via Vertx client
            // TODO: handle errors
            vertxProducer.publishMessage(event);
        } catch (IllegalStateException e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).header("APIState", "Internal Server Error").build();
        }

        // return OK
        return Response.ok().header("APIState", "VERTX_PUBLISH_OK").build();
    }
}