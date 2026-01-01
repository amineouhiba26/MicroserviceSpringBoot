package com.example.demo.filter;

import com.example.demo.security.JwtService;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtService jwtService;

    // Endpoints that require ADMIN role
    private static final List<String> ADMIN_PROTECTED_PATHS = List.of(
            "/produit-service/produits",
            "/client-service/clients",
            "/commande-service/commandes",
            "/auth-service/api/auth/users",
            "/auth-service/api/auth/roles",
            "/auth-service/api/auth/addRoleToUser"
    );

    // Public endpoints (no authentication required)
    private static final List<String> PUBLIC_PATHS = List.of(
            "/auth-service/api/auth/login"
    );

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath().toLowerCase();

        // Allow public endpoints
        if (isPublicPath(path)) {
            return chain.filter(exchange);
        }

        // Check if path requires ADMIN role
        boolean requiresAdmin = isAdminProtectedPath(path);

        // Extract Authorization header
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // No token provided - return 401
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        try {
            // Validate token
            if (!jwtService.isTokenValid(token)) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            // Extract username and roles
            String username = jwtService.extractUsername(token);
            List<String> roles = jwtService.extractRoles(token);

            // Check if ADMIN role is required and present
            if (requiresAdmin && (roles == null || !roles.contains("ADMIN"))) {
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }

            // Add username and roles as headers for downstream services
            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                    .header("X-User-Username", username)
                    .header("X-User-Roles", String.join(",", roles != null ? roles : List.of()))
                    .build();

            return chain.filter(exchange.mutate().request(modifiedRequest).build());

        } catch (Exception e) {
            // Invalid token
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    private boolean isPublicPath(String path) {
        return PUBLIC_PATHS.stream().anyMatch(path::contains);
    }

    private boolean isAdminProtectedPath(String path) {
        return ADMIN_PROTECTED_PATHS.stream().anyMatch(path::contains);
    }

    @Override
    public int getOrder() {
        return -100; // High priority
    }
}
