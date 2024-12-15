package org.project.gateway.pojo;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenericResponse<T> extends ResponsePayload{
    @JsonProperty("reqId")
    private String requestId;
    @JsonProperty("ts")
    private String timestamp;
    @JsonProperty("payload")
    private T payload;
}