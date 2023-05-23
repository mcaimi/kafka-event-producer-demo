package com.redhat.kafka;

// import common resources
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

// import event descriptor class
import com.redhat.kafka.entities.Event;

// import microprofile kafka clients
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

// wire this handler to the microprofile producer route.
// send messages to kafka using a Channel/Emitter combo
@Path("/producer")
public class EventProducerResource {
    
    @Channel("event-input-topic")
    Emitter<Event> eventGenerator;

    @POST
    @Path("/post")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postEvent(Event event) {

        // refuse events without timestamp
        if (event.event_timestamp == null) {
            return Response.status(Status.BAD_REQUEST).header("APIState", "Missing timestamp from message payload").build();
        }
        
        // send to topic
        try {
            // set ingestion time
            event.setIngestionTimestamp(); // now
            // send
            eventGenerator.send(event);
        } catch (IllegalStateException e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).header("APIState", "Internal Server Error").build();
        }

        // return OK
        return Response.ok().header("APIState", "PUBLISH_OK").build();
    }
}
