package com.redhat.kafka;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.redhat.kafka.entities.Event;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@Path("/event")
public class EventProducerResource {

    @Inject
    @Channel("event-input-topic")
    Emitter<Event> eventGenerator;

    @POST
    @Path("/send")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendEvent(Event item) {

        if (item.event_timestamp() == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }

        try {
            item.setIngestionTimestamp(); // now
            eventGenerator.send(item);
        } catch (IllegalStateException e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        
        return Response.ok().build();
    }
}
