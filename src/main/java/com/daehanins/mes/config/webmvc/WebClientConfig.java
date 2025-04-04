package com.daehanins.mes.config.webmvc;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.util.UUID;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient defaultWebClient(@Value("${app.webclient.baseUrl}") String baseUrl ) {
        TcpClient tcpClient = TcpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 2_000)
                .doOnConnected(connection ->
                        connection.addHandlerLast(new ReadTimeoutHandler(2))
                                .addHandlerLast(new WriteTimeoutHandler(2)));

        return WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
                .defaultCookie("cookieKey", "cookieValue", "teapot", "amsterdam")
                .defaultCookie("secretToken", UUID.randomUUID().toString())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.USER_AGENT, "I'm a teapot")
                .build();
    }

}
