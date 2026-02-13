package com.example.api_gateway.filter;

import com.example.api_gateway.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();
        String method = exchange.getRequest().getMethod().name();

        if (HttpMethod.OPTIONS.matches(method)) {

            exchange.getResponse().setStatusCode(HttpStatus.OK);
            return exchange.getResponse().setComplete();
        }

        if (isPublicPath(path)) {
            return chain.filter(exchange);
        }


        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }


        String token = authHeader.substring(7);


        if (!jwtUtil.validateToken(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }


        String username = jwtUtil.getUsernameFromToken(token);

     
        return chain.filter(exchange.mutate()
                .request(exchange.getRequest().mutate()
                        .header("X-User-Name", username)
                        .build())
                .build());
    }


    private boolean isPublicPath(String path) {
        boolean isPublic = path.startsWith("/security/api/v1/auth/") ||
                path.equals("/security/api/v1/auth/generate-token") ||
                path.equals("/security/api/v1/auth/google/") ||
                path.startsWith("/security/api/v1/users/register") ||
                path.contains("/v3/api-docs") ||
                path.contains("/swagger-ui");

        return isPublic;
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
