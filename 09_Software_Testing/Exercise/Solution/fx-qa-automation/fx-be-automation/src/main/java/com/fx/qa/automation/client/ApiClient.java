package com.fx.qa.automation.client;

import com.fx.qa.automation.dto.Login;
import com.fx.qa.automation.dto.Register;
import com.fx.qa.automation.dto.Transactions;
import com.fx.qa.automation.enums.Paths;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class ApiClient {
    private final WebClient webClient;
    private final String USERNAME;
    private final String PASSWORD;
    private String TOKEN;

    ApiClient(@Value("${application.url}") String url,
              @Value("${application.username}") String username,
              @Value("${application.password}") String password) {
        this.webClient = WebClient.builder()
                .baseUrl(url)
                .build();
        this.USERNAME = username;
        this.PASSWORD = password;
    }

    public int healthCheck() {
        return Optional.ofNullable(
                webClient.get()
                        .uri("/")
                        .exchangeToMono(response -> Mono.just(response.statusCode().value()))
                        .block()
        ).orElse(0);
    }

    public ClientResponse createUser(Register body) {
        return webClient.post()
                .uri(Paths.REGISTER.getValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchangeToMono(Mono::just)
                .block();
    }

    public ResponseEntity<List<String>> getCurrencies() {
        return webClient.get()
                .uri(Paths.CURRENCIES.getValue())
                .headers(headers -> headers.setBearerAuth(getToken()))
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<String>>() {})
                .block();
    }

    public ClientResponse postTransaction(Transactions body) {
        return webClient.post()
                .uri(Paths.TRANSACTIONS.getValue())
                .headers(headers -> headers.setBearerAuth(getToken()))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchangeToMono(Mono::just)
                .block();
    }

    public ResponseEntity<List<Transactions>> getTransactions() {
        ResponseEntity<List<Transactions>> response = callGetTransactions();
        if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            log.info("UNAUTHORIZED — retrying with new token");
            TOKEN = null;
            response = callGetTransactions();
        } else if (response.getBody() == null) {
            TOKEN = null;
            response = callGetTransactions();
        }
        return response;
    }

    private ResponseEntity<List<Transactions>> callGetTransactions() {
        return webClient.get()
                .uri(Paths.TRANSACTIONS.getValue())
                .headers(headers -> headers.setBearerAuth(getToken()))
                .exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode() == HttpStatus.UNAUTHORIZED) {
                        return Mono.just(new ResponseEntity<List<Transactions>>(HttpStatus.UNAUTHORIZED));
                    }
                    return clientResponse.toEntity(new ParameterizedTypeReference<List<Transactions>>() {});
                })
                .block();
    }

    private String getToken() {
        if (TOKEN == null) {
            return webClient.post()
                    .uri(Paths.AUTH.getValue())
                    .bodyValue(Login.builder()
                            .username(this.USERNAME)
                            .password(this.PASSWORD)
                            .build())
                    .retrieve()
                    .bodyToMono(Login.class)
                    .map(Login::getToken)
                    .block();
        }

        return TOKEN;
    }
}
