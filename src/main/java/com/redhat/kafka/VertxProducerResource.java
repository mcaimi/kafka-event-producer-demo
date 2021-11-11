package com.redhat.kafka;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.redhat.kafka.entities.Event;

@Path("/vertx")
public class VertxProducerResource {

    @Inject
    EventProducerService vertxProducer;

    @POST
    @Path("/post")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response vertxPost(Event event) {
        
        if (event.event_timestamp == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        
        try {
            event.setIngestionTimestamp(); // now
            vertxProducer.publishMessage(event);
        } catch (IllegalStateException e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.ok().header("APIState", "VERTX_PUBLISH_OK").build();
    }
}