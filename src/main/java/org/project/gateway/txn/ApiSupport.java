package org.project.gateway.txn;

import io.netty.channel.ConnectTimeoutException;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.WriteTimeoutException;
import lombok.Getter;
import lombok.Setter;
import org.project.gateway.pojo.RequestPayload;
import org.project.gateway.pojo.ResponsePayload;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ApiSupport {
    private Map<String, String> headers;

    private boolean isConnectTimeout;
    private boolean isReadTimeout;
    private boolean isWriteTimeout;
    private boolean requestSuccess;
    private String message;
    private RequestPayload reqPayload;
    private ResponsePayload respPayload;
    private HttpStatusCode statusCode;

    public ApiSupport(RequestPayload reqPayload) {
        this.headers = new HashMap<>();
        this.reqPayload = reqPayload;
    }

    public void populateHeaders(Map<String, String> header) {
        headers.putAll(header);
        headers.putIfAbsent(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.putIfAbsent(HttpHeaders.ACCEPT, "*/*");
    }

    public ResponsePayload process(String uri) {
        return createRequest(uri).exchangeToMono(this::handleResponse).doOnError(this::handleError).block();
    }

    private WebClient.RequestHeadersSpec<?> createRequest(String uri) {
        return HttpService.getClient().get().uri(uri).headers(httpHeaders -> headers.forEach(httpHeaders::add));
    }

    private Mono<ResponsePayload> handleResponse(ClientResponse response) {
        statusCode = response.statusCode();
        if (statusCode.is2xxSuccessful()) {
            requestSuccess = true;
            message = "successful";
            return response.bodyToMono(new ParameterizedTypeReference<>() {
            });
        } else if (statusCode.is4xxClientError()) {
            requestSuccess = false;
            message = "client error";
            return Mono.error(new HttpClientErrorException(statusCode, "client error"));
        } else if (statusCode.is5xxServerError()) {
            requestSuccess = false;
            message = "server error";
            return Mono.error(new HttpServerErrorException(statusCode, "server error"));
        } else {
            requestSuccess = false;
            message = "unexpected error";
            return Mono.error(new Exception("Unexpected error: " + statusCode));
        }
    }

    private void handleError(Throwable throwable) {
        requestSuccess = false;
        if (throwable instanceof ConnectTimeoutException) {
            isConnectTimeout = true;
        } else if (throwable instanceof ReadTimeoutException) {
            isReadTimeout = true;
        } else if (throwable instanceof WriteTimeoutException) {
            isWriteTimeout = true;
        }
    }
}
