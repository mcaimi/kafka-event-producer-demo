package com.redhat.kafka;

// import common resources
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

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
            return Response.status(Status.BAD_REQUEST).build();
        }
        
        try {
            // set ingestion timestamp
            event.setIngestionTimestamp(); // now
            // send message via Vertx client
            // TODO: handle errors
            vertxProducer.publishMessage(event);
        } catch (IllegalStateException e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }

        // return OK
        return Response.ok().header("APIState", "VERTX_PUBLISH_OK").build();
    }
}