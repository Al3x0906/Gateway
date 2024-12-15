package org.project.gateway.controllers;

import lombok.extern.slf4j.Slf4j;
import org.project.gateway.helper.ObjectSerializer;
import org.project.gateway.pojo.RequestPayload;
import org.project.gateway.pojo.ResponsePayload;
import org.project.gateway.txn.ApiSupport;
import org.project.gateway.txn.EndpointsParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@SuppressWarnings("unused")
@Controller
public class App {


    @Autowired
    RedisTemplate redisTemplate;

    @GetMapping(path = "/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> api() {
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).body("OK");
    }


    @PostMapping(path = "/{reqName}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> genericEndpoint(
            @PathVariable("reqName") String reqName,
            @RequestBody String data,
            @RequestHeader Map<String, String> headers) {
        RequestPayload requestPayload = ObjectSerializer.fromJson(data, RequestPayload.class);
        ApiSupport apiSupport = new ApiSupport(requestPayload);
        apiSupport.populateHeaders(headers);
        ResponsePayload resp = apiSupport.process(EndpointsParser.getApiEndpoints(reqName));
        return ResponseEntity.ok(resp);
    }

}
