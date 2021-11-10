package com.redhat.kafka.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.UUID;
import java.text.SimpleDateFormat;

@JsonIgnoreProperties(ignoreUnknown = false)
public class Event {
    
    private enum EVENT_SEVERITY { NORMAL, INFO, WARNING, ERROR, CRITICAL };
    private final SimpleDateFormat timestamp_formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    public UUID id;
    public String message;
    public EVENT_SEVERITY severity;
    public Long event_timestamp;
    public Long ingestion_timestamp;

    // constructors
    public Event() { }

    public Event(String id, String message, EVENT_SEVERITY severity, Long event_timestamp) {
        this.id = UUID.fromString(id);
        this.message = message;
        this.severity = severity;
        this.event_timestamp = event_timestamp;
    }

    // setters
    public void setId(String newId) { this.id = UUID.fromString(newId); }
    public void setMessage(String newMsg) { this.message = newMsg; }
    public void setSeverity(EVENT_SEVERITY newSev) { this.severity = newSev; }
    public void setIngestionTimestamp() { this.ingestion_timestamp = new Date().getTime(); }

    // serializer
    @Override
    public String toString() {
        return "Event( " + 
                "'id': " + this.id + 
                ", 'message': " + this.message + 
                ", 'severity': " + this.severity + 
                ", 'ingestion_timestamp': " + this.timestamp_formatter.format(this.ingestion_timestamp) +
                ", 'event_timestamp': " + this.timestamp_formatter.format(this.event_timestamp) +
                " )";
    }
}
