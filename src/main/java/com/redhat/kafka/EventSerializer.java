package com.redhat.kafka;

import com.redhat.kafka.entities.Event;
import io.quarkus.kafka.client.serialization.ObjectMapperSerializer;

public class EventSerializer extends ObjectMapperSerializer<Event> {
    
}
