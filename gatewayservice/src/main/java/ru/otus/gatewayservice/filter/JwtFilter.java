package ru.otus.gatewayservice.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.otus.gatewayservice.service.JwtTokenService;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter implements GatewayFilter {

    @Autowired
    private final JwtTokenService jwtTokenService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Optional<String> token = getToken(exchange.getRequest().getHeaders());

        if (token.isEmpty()) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        } else {
            Optional<String> tokenLogin = jwtTokenService.getLogin(token.get());

            if (tokenLogin.isPresent()) {
                Map<String, String> uriVariables = ServerWebExchangeUtils.getUriTemplateVariables(exchange);
                String pathLogin = uriVariables.get("login");

                if (tokenLogin.get().equals(pathLogin)) {
                    return chain.filter(exchange);
                } else {
                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete();
                }
            } else {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        }
    }

    private Optional<String> getToken(HttpHeaders httpHeaders) {
        log.info("Headers: " + httpHeaders.toString());

        return httpHeaders.entrySet().stream()
                .filter(entry -> entry.getKey().equalsIgnoreCase(HttpHeaders.AUTHORIZATION))
                .flatMap(entry -> entry.getValue().stream())
                .map(value -> {
                    return value.replaceAll("Bearer\\s+", "");
                }).findFirst();
    }
}
