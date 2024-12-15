package org.project.gateway.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class GenericRequest<T> extends RequestPayload{
    @JsonProperty("reqId")
    private String requestId;
    @JsonProperty("ts")
    private LocalDateTime timestamp;
    @JsonProperty("payload")
    private T payload;

    public GenericRequest() {
        this.timestamp = LocalDateTime.now(); // Get the current local date and time
    }
}
