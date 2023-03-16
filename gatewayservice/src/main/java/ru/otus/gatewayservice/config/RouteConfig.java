package ru.otus.gatewayservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.gatewayservice.filter.JwtFilter;

@Configuration
public class RouteConfig {

    @Value("${services.profileServiceUrl}")
    private String profileServiceUrl;

    @Bean
    public RouteLocator nonSecuredPath(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("signUp",
                        route -> route.path("/signUp")
                                .filters(f -> f.rewritePath("/signUp", "/api/users/signUp"))
                                .uri(profileServiceUrl))
                .route("login",
                        route -> route.path("/login")
                                .filters(f -> f.rewritePath("/login", "/api/users/login"))
                                .uri(profileServiceUrl))
                .build();
    }

    @Bean
    public RouteLocator securedPath(RouteLocatorBuilder builder, JwtFilter jwtFilter) {
        return builder.routes()
                .route("updateUser",
                        route -> route.path("/users/update/{login}")
                                .filters(f -> f.filter(jwtFilter)
                                        .rewritePath("/users/update/(?<segment>.*)", "/api/users/update/${segment}"))
                                .uri(profileServiceUrl))
                .route("getUser",
                        route -> route.path("/users/{login}")
                                .filters(f -> f.filter(jwtFilter)
                                        .rewritePath("/users/(?<segment>.*)", "/api/users/${segment}"))
                                .uri(profileServiceUrl))
                .build();
    }
}
