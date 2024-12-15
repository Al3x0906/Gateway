package org.project.gateway.txn;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@SuppressWarnings("unused")
public class HttpService {
    public static final int DEFAULT_CONN_TIMEOUT = 10_000;
    public static final int DEFAULT_READ_TIMEOUT = 10;
    public static final int DEFAULT_WRITE_TIMEOUT = 10;

    private HttpService() {
    }

    public static WebClient getClient() {
        return createWebClient(DEFAULT_CONN_TIMEOUT, DEFAULT_READ_TIMEOUT, DEFAULT_WRITE_TIMEOUT);
    }


    public static WebClient getClient(int connectionTimeout) {
        return createWebClient(connectionTimeout, DEFAULT_READ_TIMEOUT, DEFAULT_WRITE_TIMEOUT);
    }

    public static WebClient getClient(int connectionTimeout, int readTimeout) {
        return createWebClient(connectionTimeout, readTimeout, DEFAULT_WRITE_TIMEOUT);
    }

    public static WebClient getClient(int connectionTimeout, int readTimeout, int writeTimeout) {
        return createWebClient(connectionTimeout, readTimeout, writeTimeout);
    }

    private static WebClient createWebClient(int connectionTimeout, int readTimeout, int writeTimeout) {
        HttpClient httpClient = HttpClient.create().
                option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionTimeout)
                .doOnConnected(conn -> conn.
                        addHandlerLast(
                                new ReadTimeoutHandler(readTimeout)
                        )
                        .addHandlerLast(
                                new WriteTimeoutHandler(writeTimeout)
                        )
                );

        return WebClient.builder().
                clientConnector(
                        new ReactorClientHttpConnector(
                                httpClient)
                )
                .build();
    }
}
