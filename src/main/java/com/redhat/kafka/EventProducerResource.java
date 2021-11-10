package com.redhat.kafka;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.redhat.kafka.entities.Event;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@Path("/producer")
public class EventProducerResource {

    @Channel("event-input-topic")
    Emitter<Event> eventGenerator;

    @POST
    @Path("/post")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postEvent(Event event) {

        if (event.event_timestamp == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        
        
        try {
            event.setIngestionTimestamp(); // now
            eventGenerator.send(event);
        } catch (IllegalStateException e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        System.out.println(event);
        return Response.ok().build();
    }
}
