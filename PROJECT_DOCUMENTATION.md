# 📚 Spring Boot Microservices Project - Complete Documentation

> **A comprehensive guide to building a microservices architecture with Spring Boot, Spring Cloud, JWT Security, and AI Integration using MCP (Model Context Protocol)**

---

## 📋 Table of Contents

1. [Project Overview](#1-project-overview)
2. [Architecture Diagram](#2-architecture-diagram)
3. [Technology Stack](#3-technology-stack)
4. [Project Structure](#4-project-structure)
5. [Discovery Service (Eureka)](#5-discovery-service-eureka)
6. [API Gateway](#6-api-gateway)
7. [Auth Service](#7-auth-service)
8. [Client Service](#8-client-service)
9. [Product Service (Produit)](#9-product-service-produit)
10. [Order Service (Commande)](#10-order-service-commande)
11. [AI Agent Service](#11-ai-agent-service)
12. [Security Implementation](#12-security-implementation)
13. [Inter-Service Communication](#13-inter-service-communication)
14. [MCP Integration](#14-mcp-integration)
15. [Running the Project](#15-running-the-project)
16. [API Testing](#16-api-testing)
17. [Frontend Integration](#17-frontend-integration)

---

## 1. Project Overview

This project implements a **microservices architecture** for an e-commerce system with the following capabilities:

- ✅ **Service Discovery** using Netflix Eureka
- ✅ **API Gateway** for routing and security
- ✅ **JWT-based Authentication** with role-based access control
- ✅ **Inter-service Communication** using OpenFeign
- ✅ **AI Integration** using Spring AI with Ollama and MCP
- ✅ **PostgreSQL Database** for data persistence
- ✅ **Angular Frontend** (optional)

### Services Overview

| Service | Port | Description |
|---------|------|-------------|
| Discovery Service | 8761 | Eureka Server for service registration |
| API Gateway | 8888 | Single entry point, routing, security |
| Auth Service | 8080 | Authentication & JWT token generation |
| Client Service | 8082 | Client/Customer management (MCP Server) |
| Product Service | 9081 | Product inventory management (MCP Server) |
| Order Service | 9092 | Order management (MCP Server) |
| AI Agent Service | 8081 | AI Chat with MCP integration |

---

## 2. Architecture Diagram

```
                                    ┌─────────────────────────────────────┐
                                    │         Discovery Service           │
                                    │         (Eureka - 8761)             │
                                    └──────────────┬──────────────────────┘
                                                   │ Register/Discover
                    ┌──────────────────────────────┼──────────────────────────────┐
                    │                              │                              │
┌───────────────────▼───┐    ┌────────────────────▼────┐    ┌────────────────────▼───┐
│   Client Service      │    │    Product Service      │    │    Order Service       │
│   (8082) - MCP Server │    │    (9081) - MCP Server  │    │    (9092) - MCP Server │
└───────────────────────┘    └─────────────────────────┘    └────────────────────────┘
           ▲                            ▲                              ▲
           │                            │                              │
           │         ┌──────────────────┴──────────────────┐           │
           │         │         API Gateway (8888)          │           │
           └─────────┤   - JWT Validation                  ├───────────┘
                     │   - Request Routing                 │
                     │   - CORS Handling                   │
                     └─────────────────┬───────────────────┘
                                       │
           ┌───────────────────────────┼───────────────────────────┐
           │                           │                           │
┌──────────▼──────────┐    ┌───────────▼───────────┐    ┌─────────▼──────────┐
│   Auth Service      │    │    AI Agent Service   │    │   Frontend         │
│   (8080)            │    │    (8081) - MCP Client│    │   (Angular)        │
│   - JWT Generation  │    │    - Ollama/LLM       │    └────────────────────┘
└─────────────────────┘    └───────────────────────┘
```

---

## 3. Technology Stack

### Parent POM Configuration

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.3.5</version>
    </parent>

    <groupId>org.example</groupId>
    <artifactId>tpmicroservice</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>pom</packaging>

    <modules>
        <module>api-gateway</module>
        <module>auth-service</module>
        <module>client-service</module>
        <module>commande-service</module>
        <module>discovery-service</module>
        <module>produit</module>
        <module>agent-ia-service</module>
    </modules>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <spring-boot.version>3.3.5</spring-boot.version>
        <spring-cloud.version>2023.0.3</spring-cloud.version>
        <spring-ai.version>1.1.1</spring-ai.version>
    </properties>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.ai</groupId>
                <artifactId>spring-ai-bom</artifactId>
                <version>${spring-ai.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
</project>
```

### Key Technologies

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 17 | Programming Language |
| Spring Boot | 3.3.5 | Application Framework |
| Spring Cloud | 2023.0.3 | Microservices Tools |
| Spring AI | 1.1.1 | AI/LLM Integration |
| PostgreSQL | Latest | Database |
| Ollama | Latest | Local LLM Runtime |
| JWT (jjwt) | 0.12.6 | Token-based Security |

---

## 4. Project Structure

```
MicroserviceSpringBoot/
├── pom.xml                          # Parent POM
├── discovery-service/               # Eureka Server
│   ├── pom.xml
│   └── src/main/java/.../DemoApplication.java
├── api-gateway/                     # Spring Cloud Gateway
│   ├── pom.xml
│   └── src/main/java/
│       └── com/example/demo/
│           ├── GatewayserviceApplication.java
│           └── filter/AuthFilter.java
├── auth-service/                    # Authentication Service
│   ├── pom.xml
│   └── src/main/java/
│       └── com/example/auth/
│           ├── entities/
│           ├── repo/
│           ├── service/
│           ├── security/
│           └── web/
├── client-service/                  # Client Management (MCP Server)
│   ├── pom.xml
│   └── src/main/java/
│       └── com/example/demo/
│           ├── entities/
│           ├── repository/
│           └── controller/
├── produit/                         # Product Management (MCP Server)
│   ├── pom.xml
│   └── src/main/java/
│       └── com/example/demo/
│           ├── produits/
│           ├── repository/
│           └── controller/
├── commande-service/                # Order Management (MCP Server)
│   ├── pom.xml
│   └── src/main/java/
│       └── com/example/demo/
│           ├── entities/
│           ├── model/
│           ├── feign/
│           ├── repository/
│           └── controller/
├── agent-ia-service/                # AI Agent (MCP Client)
│   ├── pom.xml
│   └── src/main/java/
│       └── com/example/agentia/
│           ├── config/
│           └── controller/
└── frontend/                        # Angular Frontend
    └── src/app/
```

---

## 5. Discovery Service (Eureka)

The Discovery Service acts as a **service registry** where all microservices register themselves.

### Configuration (`application.properties`)

```properties
spring.application.name=demo
server.port=8761
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=true
```

### Main Application

```java
package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```

### Key Annotations
- `@EnableEurekaServer` - Enables this application as a Eureka Server

### Access
- **URL**: http://localhost:8761
- **Purpose**: View all registered services

---

## 6. API Gateway

The API Gateway is the **single entry point** for all client requests. It handles:
- Request routing to appropriate services
- JWT token validation
- CORS configuration

### Configuration (`application.properties`)

```properties
spring.application.name=gatewayservice
server.port=8888

# Eureka Client
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/

# Dynamic Route Discovery
spring.cloud.gateway.discovery.locator.lower-case-service-id=true
spring.cloud.gateway.discovery.locator.enabled=true

# CORS Configuration
spring.cloud.gateway.globalcors.corsConfigurations.[/**].allowedOrigins=*
spring.cloud.gateway.globalcors.corsConfigurations.[/**].allowedMethods=*
spring.cloud.gateway.globalcors.corsConfigurations.[/**].allowedHeaders=*
```

### Main Application

```java
package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GatewayserviceApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayserviceApplication.class, args);
    }
}
```

### JWT Authentication Filter

```java
package com.example.demo.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class AuthFilter implements GlobalFilter, Ordered {

    // Must match the secret in Auth Service
    private static final String SECRET = "secret12345678901234567890123456789012";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        // Public Endpoints - Allow without authentication
        if (path.contains("/auth-service/login") || 
            (path.contains("/auth-service/users") && exchange.getRequest().getMethod().name().equals("POST")) ||
            (path.contains("/auth-service/roles") && exchange.getRequest().getMethod().name().equals("POST"))) {
            return chain.filter(exchange);
        }

        // Check for Authorization Header
        if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        try {
            // Validate Token
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            // Forward user info to downstream services
            exchange.getRequest().mutate()
                    .header("X-Auth-User", claims.getSubject())
                    .build();

        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1; // High priority
    }
}
```

---

## 7. Auth Service

The Auth Service handles **user authentication**, **role management**, and **JWT token generation**.

### Configuration (`application.properties`)

```properties
spring.application.name=AUTH-SERVICE
server.port=8080

# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/tpMicroservice?createDatabaseIfNotExist=true
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Eureka
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
```

### Entities

#### AppUser.java
```java
package com.example.auth.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class AppUser {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String username;
    
    private String password;
    
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<AppRole> roles = new ArrayList<>();
}
```

#### AppRole.java
```java
package com.example.auth.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class AppRole {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roleName;
}
```

### Repositories

```java
// AppUserRepository.java
package com.example.auth.repo;

import com.example.auth.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    AppUser findByUsername(String username);
}

// AppRoleRepository.java
package com.example.auth.repo;

import com.example.auth.entities.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppRoleRepository extends JpaRepository<AppRole, Long> {
    AppRole findByRoleName(String roleName);
}
```

### Service Layer

#### AccountService.java (Interface)
```java
package com.example.auth.service;

import com.example.auth.entities.AppRole;
import com.example.auth.entities.AppUser;
import java.util.List;

public interface AccountService {
    AppUser addNewUser(AppUser appUser);
    AppRole addNewRole(AppRole appRole);
    void addRoleToUser(String username, String roleName);
    AppUser loadUserByUsername(String username);
    List<AppUser> listUsers();
}
```

#### AccountServiceImpl.java
```java
package com.example.auth.service;

import com.example.auth.entities.AppRole;
import com.example.auth.entities.AppUser;
import com.example.auth.repo.AppRoleRepository;
import com.example.auth.repo.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {
    private AppUserRepository appUserRepository;
    private AppRoleRepository appRoleRepository;
    private PasswordEncoder passwordEncoder;

    public AccountServiceImpl(AppUserRepository appUserRepository, 
                              AppRoleRepository appRoleRepository, 
                              PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.appRoleRepository = appRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AppUser addNewUser(AppUser appUser) {
        String pw = appUser.getPassword();
        appUser.setPassword(passwordEncoder.encode(pw));
        return appUserRepository.save(appUser);
    }

    @Override
    public AppRole addNewRole(AppRole appRole) {
        return appRoleRepository.save(appRole);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        AppUser appUser = appUserRepository.findByUsername(username);
        AppRole appRole = appRoleRepository.findByRoleName(roleName);
        appUser.getRoles().add(appRole);
    }

    @Override
    public AppUser loadUserByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    @Override
    public List<AppUser> listUsers() {
        return appUserRepository.findAll();
    }
}
```

#### UserDetailsServiceImpl.java
```java
package com.example.auth.service;

import com.example.auth.entities.AppUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private AccountService accountService;

    public UserDetailsServiceImpl(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = accountService.loadUserByUsername(username);
        if(appUser == null) throw new UsernameNotFoundException("User not found");
        
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        appUser.getRoles().forEach(r -> {
            authorities.add(new SimpleGrantedAuthority(r.getRoleName()));
        });
        
        return new User(appUser.getUsername(), appUser.getPassword(), authorities);
    }
}
```

### Security Configuration

```java
package com.example.auth.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());
        http.formLogin(form -> form.disable());
        http.httpBasic(basic -> basic.disable());
        http.sessionManagement(session -> 
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        http.headers(headers -> headers.frameOptions(frame -> frame.disable()));
        return http.build();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
```

### Controllers

#### AuthController.java (Login & JWT Generation)
```java
package com.example.auth.web;

import com.example.auth.service.AccountService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class AuthController {
    private AuthenticationManager authenticationManager;
    private AccountService accountService;

    public AuthController(AuthenticationManager authenticationManager, AccountService accountService) {
        this.authenticationManager = authenticationManager;
        this.accountService = accountService;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> credentials, 
                                     HttpServletRequest request) {
        String username = credentials.get("username");
        String password = credentials.get("password");
        
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        
        User user = (User) authentication.getPrincipal();
        String algorithm = "secret12345678901234567890123456789012"; // HS256 requires 256 bits
        
        String jwtAccessToken = Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + 5 * 60 * 1000)) // 5 min
                .setIssuer(request.getRequestURL().toString())
                .claim("roles", user.getAuthorities().stream()
                    .map(ga -> ga.getAuthority())
                    .collect(Collectors.toList()))
                .signWith(Keys.hmacShaKeyFor(algorithm.getBytes(StandardCharsets.UTF_8)))
                .compact();
                
        Map<String, String> idToken = new HashMap<>();
        idToken.put("access-token", jwtAccessToken);
        return idToken;
    }
}
```

#### AccountRestController.java (User & Role Management)
```java
package com.example.auth.web;

import com.example.auth.entities.AppRole;
import com.example.auth.entities.AppUser;
import com.example.auth.service.AccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AccountRestController {
    private AccountService accountService;

    public AccountRestController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/users")
    public List<AppUser> appUsers() {
        return accountService.listUsers();
    }

    @PostMapping("/users")
    public AppUser saveUser(@RequestBody AppUser appUser) {
        return accountService.addNewUser(appUser);
    }

    @PostMapping("/roles")
    public AppRole saveRole(@RequestBody AppRole appRole) {
        return accountService.addNewRole(appRole);
    }

    @PostMapping("/addRoleToUser")
    public void addRoleToUser(@RequestBody RoleUserForm roleUserForm) {
        accountService.addRoleToUser(roleUserForm.getUsername(), roleUserForm.getRoleName());
    }
}

class RoleUserForm {
    private String username;
    private String roleName;
    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }
}
```

### Main Application

```java
package com.example.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableDiscoveryClient
public class AuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

---

## 8. Client Service

The Client Service manages customer data and exposes **MCP tools** for AI integration.

### Configuration (`application.properties`)

```properties
spring.application.name=CLIENT-SERVICE
server.port=8082

# MCP Server Configuration
spring.ai.mcp.server.protocol=streamable

# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/tpMicroservice?createDatabaseIfNotExist=true
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Eureka
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/

# Actuator
management.endpoints.web.exposure.include=*
```

### Entity

```java
package com.example.demo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Client {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
}
```

### Controller with MCP Annotations

```java
package com.example.demo.controller;

import com.example.demo.entities.Client;
import com.example.demo.repository.ClientRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;

import java.util.List;

@RestController
public class ClientController {
    private ClientRepository clientRepository;

    public ClientController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @McpTool(name = "getClients", description = "Récupère la liste de tous les clients")
    @GetMapping("/clients")
    public List<Client> clients() {
        return clientRepository.findAll();
    }

    @McpTool(name = "getClient", description = "Récupère un client via son ID")
    @GetMapping("/clients/{id}")
    public Client client(@McpToolParam(description = "ID du client") @PathVariable Long id) {
        return clientRepository.findById(id).orElse(null);
    }
}
```

### Main Application with Data Initialization

```java
package com.example.demo;

import com.example.demo.entities.Client;
import com.example.demo.repository.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ClientServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner start(ClientRepository clientRepository) {
        return args -> {
            clientRepository.save(new Client(null, "Amine", "amine@gmail.com"));
            clientRepository.save(new Client(null, "Hassan", "hassan@gmail.com"));
            clientRepository.save(new Client(null, "Mohamed", "mohamed@gmail.com"));
        };
    }
}
```

---

## 9. Product Service (Produit)

The Product Service manages product inventory with full **CRUD operations** and **MCP integration**.

### Configuration (`application.properties`)

```properties
spring.application.name=PRODUIT-SERVICE
server.port=9081

# MCP Configuration
spring.ai.mcp.server.protocol=streamable

# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/tpMicroservice?createDatabaseIfNotExist=true
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Eureka
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
management.endpoints.web.exposure.include=*
```

### Entity

```java
package com.example.demo.produits;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nom;
    private double prix;
}
```

### Controller with MCP Tools

```java
package com.example.demo.controller;

import com.example.demo.produits.Produit;
import com.example.demo.repository.ProduitRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;

@AllArgsConstructor
@RestController
public class ProduitController {
    private ProduitRepository produitRepository;

    @McpTool(name = "getProducts", description = "Récupère la liste de tous les produits")
    @GetMapping("/produits")
    public List<Produit> all() {
        return produitRepository.findAll();
    }

    @McpTool(name = "getProduct", description = "Récupère un produit via son ID")
    @GetMapping("/produits/{id}")
    public Produit get(@McpToolParam(description = "ID du produit") @PathVariable long id) {
        return produitRepository.findById(id).get();
    }

    @McpTool(name = "createProduct", description = "Crée un nouveau produit avec un nom et un prix")
    @PostMapping("/produits")
    public Produit create(
            @McpToolParam(description = "Nom du produit") @RequestParam String nom,
            @McpToolParam(description = "Prix du produit") @RequestParam double prix) {
        Produit produit = new Produit();
        produit.setNom(nom);
        produit.setPrix(prix);
        return produitRepository.save(produit);
    }
    
    @McpTool(name = "deleteProduct", description = "Supprime un produit via son ID")
    @DeleteMapping("/produits/{id}")
    public void delete(@McpToolParam(description = "ID du produit") @PathVariable long id) {
        produitRepository.deleteById(id);
    }
}
```

---

## 10. Order Service (Commande)

The Order Service orchestrates orders by communicating with Client and Product services using **OpenFeign**.

### Configuration (`application.properties`)

```properties
spring.application.name=commande-service
server.port=9092

# MCP Configuration
spring.ai.mcp.server.protocol=streamable

# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/tpMicroservice?createDatabaseIfNotExist=true
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Eureka
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
```

### Entities

#### Commande.java
```java
package com.example.demo.entities;

import com.example.demo.model.Client;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date date;
    private Long idClient;

    @Transient  // Not persisted, fetched from Client Service
    private Client client;

    @OneToMany(mappedBy = "commande")
    private List<ProductItem> productItems;
}
```

#### ProductItem.java
```java
package com.example.demo.entities;

import com.example.demo.model.Produit;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long idProduit;
    private int quantite;
    private double prix;

    @Transient  // Not persisted, fetched from Product Service
    private Produit produit;

    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Commande commande;
}
```

### Feign Clients

```java
// ClientRestClient.java
package com.example.demo.feign;

import com.example.demo.model.Client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "CLIENT-SERVICE")
public interface ClientRestClient {
    @GetMapping("/clients/{id}")
    Client findClientById(@PathVariable Long id);

    @GetMapping("/clients")
    List<Client> getClients();
}

// ProduitRestClient.java
package com.example.demo.feign;

import com.example.demo.model.Produit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "PRODUIT-SERVICE")
public interface ProduitRestClient {
    @GetMapping("/produits/{id}")
    Produit findProduitById(@PathVariable Long id);

    @GetMapping("/produits")
    List<Produit> getProduits();
}
```

### Controller

```java
package com.example.demo.controller;

import com.example.demo.entities.Commande;
import com.example.demo.feign.ClientRestClient;
import com.example.demo.feign.ProduitRestClient;
import com.example.demo.model.Client;
import com.example.demo.model.Produit;
import com.example.demo.repository.CommandeRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;

@RestController
@AllArgsConstructor
public class CommandeController {
    private CommandeRepository commandeRepository;
    private ClientRestClient clientRestClient;
    private ProduitRestClient produitRestClient;

    @GetMapping("/commandes")
    public List<Commande> all() {
        return commandeRepository.findAll();
    }
    
    @McpTool(name = "getOrders", description = "Récupère la liste des 20 dernières commandes")
    @GetMapping("/commandes/recent")
    public List<Map<String, Object>> recentOrders() {
        List<Commande> all = commandeRepository.findAll();
        int size = all.size();
        List<Commande> recent = all.subList(Math.max(0, size - 20), size);
        
        return recent.stream().map(c -> {
            Map<String, Object> summary = new HashMap<>();
            summary.put("id", c.getId());
            summary.put("date", c.getDate());
            summary.put("idClient", c.getIdClient());
            summary.put("nbProduits", c.getProductItems() != null ? c.getProductItems().size() : 0);
            double total = c.getProductItems() != null ? 
                c.getProductItems().stream().mapToDouble(pi -> pi.getPrix() * pi.getQuantite()).sum() : 0;
            summary.put("total", total);
            return summary;
        }).toList();
    }

    @McpTool(name = "getOrderDetails", description = "Récupère les détails d'une commande")
    @GetMapping("/commandes/{id}")
    public Commande getCommandeDetails(@McpToolParam(description = "ID de la commande") @PathVariable Long id) {
        Commande commande = commandeRepository.findById(id).orElse(null);
        if (commande == null) return null;

        // Fetch client data from Client Service
        Client client = clientRestClient.findClientById(commande.getIdClient());
        commande.setClient(client);

        // Fetch product data for each item from Product Service
        commande.getProductItems().forEach(pi -> {
            Produit produit = produitRestClient.findProduitById(pi.getIdProduit());
            pi.setProduit(produit);
        });

        return commande;
    }
}
```

### Main Application

```java
package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients 
@SpringBootApplication
public class CommandeServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommandeServiceApplication.class, args);
    }
}
```

---

## 11. AI Agent Service

The AI Agent Service provides a **conversational AI interface** using Spring AI with Ollama, connecting to other services via **MCP (Model Context Protocol)**.

### Dependencies (`pom.xml`)

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-webflux</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.ai</groupId>
        <artifactId>spring-ai-starter-model-ollama</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.ai</groupId>
        <artifactId>spring-ai-starter-mcp-client</artifactId>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>0.12.6</version>
    </dependency>
</dependencies>
```

### Configuration (`application.properties`)

```properties
spring.application.name=AGENT-IA-SERVICE
server.port=8081

# Eureka
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/

# Ollama Configuration
spring.ai.ollama.base-url=http://localhost:11434
spring.ai.ollama.chat.options.model=llama3.2

# MCP Client Configuration - Connect to MCP Servers
spring.ai.mcp.client.streamable-http.connections.client-service.url=http://localhost:8082
spring.ai.mcp.client.streamable-http.connections.client-service.endpoint=/mcp
spring.ai.mcp.client.streamable-http.connections.product-service.url=http://localhost:9081
spring.ai.mcp.client.streamable-http.connections.product-service.endpoint=/mcp
spring.ai.mcp.client.streamable-http.connections.commande-service.url=http://localhost:9092
spring.ai.mcp.client.streamable-http.connections.commande-service.endpoint=/mcp

# Logging
logging.level.org.springframework.ai=DEBUG
```

### Main Application

```java
package com.example.agentia;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class AgentIaServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgentIaServiceApplication.class, args);
    }

    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .maxMessages(10)
                .build();
    }
}
```

### Chat Controller with Role-Based Access

```java
package com.example.agentia.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@RestController
public class ChatController {

    private static final String JWT_SECRET = "secret12345678901234567890123456789012";

    private boolean isAdmin(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return false;
        }
        try {
            String token = authHeader.substring(7);
            Claims claims = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            List<String> roles = claims.get("roles", List.class);
            return roles != null && roles.contains("ADMIN");
        } catch (Exception e) {
            return false;
        }
    }

    enum Intent {
        LIST_PRODUCTS, GET_PRODUCT, LIST_CLIENTS, LIST_ORDERS,
        CREATE_PRODUCT, UPDATE_PRODUCT, DELETE_PRODUCT, UNKNOWN
    }

    private Intent classifyIntent(String message) {
        String m = message.toLowerCase();
        if (m.contains("product") || m.contains("produit")) {
            if (m.contains("create") || m.contains("add")) return Intent.CREATE_PRODUCT;
            if (m.contains("delete") || m.contains("remove")) return Intent.DELETE_PRODUCT;
            return Intent.LIST_PRODUCTS;
        }
        if (m.contains("client")) return Intent.LIST_CLIENTS;
        if (m.contains("order") || m.contains("commande")) return Intent.LIST_ORDERS;
        return Intent.UNKNOWN;
    }

    private static final Map<Intent, Boolean> USER_PERMISSIONS = Map.of(
            Intent.LIST_PRODUCTS, true,
            Intent.GET_PRODUCT, true,
            Intent.UNKNOWN, true
    );

    private boolean isAllowed(boolean admin, Intent intent) {
        if (admin) return true;
        return USER_PERMISSIONS.getOrDefault(intent, false);
    }

    private static final String ADMIN_PROMPT = """
        You display business data to administrators.
        Rules:
        - Always use tools to fetch real data
        - Show results as a simple numbered list
        - Never invent data
        """;

    private static final String USER_PROMPT = """
        You display product data to users.
        Rules:
        - You may ONLY show products
        - Always use tools
        - Never invent data
        """;

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder builder,
                          SyncMcpToolCallbackProvider mcpToolCallbackProvider) {
        this.chatClient = builder
                .defaultAdvisors(userMemoryAdvisor())
                .defaultToolCallbacks(mcpToolCallbackProvider)
                .build();
    }

    private MessageChatMemoryAdvisor userMemoryAdvisor() {
        ChatMemory memory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .maxMessages(10)
                .build();
        return MessageChatMemoryAdvisor.builder(memory).build();
    }

    @GetMapping("/chat")
    public String chat(@RequestParam String message,
                       @RequestHeader(value = "Authorization", required = false) String authHeader) {
        boolean admin = isAdmin(authHeader);
        Intent intent = classifyIntent(message);

        if (!isAllowed(admin, intent)) {
            return "🚫 Access Denied - You don't have permission for this action.";
        }

        String systemPrompt = admin ? ADMIN_PROMPT : USER_PROMPT;

        return chatClient.prompt()
                .system(systemPrompt)
                .user(message)
                .call()
                .content();
    }
}
```

---

## 12. Security Implementation

### JWT Token Structure

```json
{
  "sub": "admin",
  "roles": ["ADMIN", "USER"],
  "exp": 1704672000,
  "iss": "http://localhost:8080/login"
}
```

### Security Flow

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                              SECURITY FLOW                                   │
└─────────────────────────────────────────────────────────────────────────────┘

1. USER LOGIN
   Client → POST /auth-service/login {username, password}
   Auth Service → Validate credentials
   Auth Service → Generate JWT with roles
   Auth Service → Return {access-token: "eyJ..."}

2. AUTHENTICATED REQUEST
   Client → GET /client-service/clients (Header: Authorization: Bearer eyJ...)
   API Gateway → Extract token
   API Gateway → Validate signature with secret key
   API Gateway → Check expiration
   API Gateway → Forward request to Client Service
   Client Service → Return data

3. UNAUTHORIZED REQUEST
   Client → GET /client-service/clients (No token)
   API Gateway → 401 Unauthorized
```

### Key Security Components

| Component | Location | Purpose |
|-----------|----------|---------|
| JWT Secret | Auth Service, Gateway, AI Agent | Token signing/validation |
| BCrypt | Auth Service | Password hashing |
| AuthFilter | API Gateway | Token validation filter |
| SecurityConfig | Auth Service | Spring Security setup |

---

## 13. Inter-Service Communication

### Synchronous Communication (Feign)

```
┌──────────────────┐     ┌──────────────────┐     ┌──────────────────┐
│   Order Service  │────▶│  Eureka Server   │◀────│  Client Service  │
│                  │     │  (Discovery)     │     │                  │
│  @FeignClient    │     └────────┬─────────┘     │                  │
│  ("CLIENT-      │              │                │                  │
│   SERVICE")     │              │                │                  │
│                  │──────────────────────────────▶│  /clients/{id}   │
│                  │       HTTP GET                │                  │
│                  │◀──────────────────────────────│  Client JSON     │
└──────────────────┘                               └──────────────────┘
```

### Key Points
- **Service Discovery**: Services register with Eureka and discover each other by name
- **Load Balancing**: Feign automatically load-balances requests
- **Resilience**: Can be enhanced with Circuit Breaker (Resilience4j)

---

## 14. MCP Integration

### What is MCP?
**Model Context Protocol (MCP)** is a standard for connecting AI models to external tools and data sources.

### MCP Architecture in This Project

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           MCP ARCHITECTURE                                   │
└─────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────┐
│   AI Agent Service  │
│   (MCP Client)      │
│                     │
│   ChatClient with   │
│   MCP Tool Provider │
└────────┬────────────┘
         │
         │ Streamable HTTP
         │
         ▼
┌────────┴────────────┬─────────────────────┬─────────────────────┐
│                     │                     │                     │
▼                     ▼                     ▼                     
┌─────────────┐  ┌─────────────┐  ┌─────────────┐
│   Client    │  │   Product   │  │   Order     │
│   Service   │  │   Service   │  │   Service   │
│ (MCP Server)│  │ (MCP Server)│  │ (MCP Server)│
│             │  │             │  │             │
│ @McpTool:   │  │ @McpTool:   │  │ @McpTool:   │
│ -getClients │  │ -getProducts│  │ -getOrders  │
│ -getClient  │  │ -getProduct │  │ -getOrder   │
│             │  │ -createProd │  │             │
│             │  │ -deleteProd │  │             │
└─────────────┘  └─────────────┘  └─────────────┘
```

### MCP Server Configuration

```java
// In Controller - Mark methods as MCP tools
@McpTool(name = "getProducts", description = "Retrieves all products")
@GetMapping("/produits")
public List<Produit> all() {
    return produitRepository.findAll();
}
```

### MCP Client Configuration

```properties
# Connect to MCP servers
spring.ai.mcp.client.streamable-http.connections.product-service.url=http://localhost:9081
spring.ai.mcp.client.streamable-http.connections.product-service.endpoint=/mcp
```

---

## 15. Running the Project

### Prerequisites

```bash
# Check Java 17
java -version

# Check Maven
mvn -version

# Check PostgreSQL
pg_isready -h localhost -p 5432

# Check Ollama (for AI service)
curl http://localhost:11434/api/tags
```

### Step 1: Database Setup

```sql
psql -U postgres
CREATE DATABASE tpMicroservice;
\q
```

### Step 2: Build All Services

```bash
cd /path/to/MicroserviceSpringBoot
mvn clean package -DskipTests
```

### Step 3: Start Services (In Order)

```bash
# 1. Discovery Service
java -jar discovery-service/target/discovery-service-0.0.1-SNAPSHOT.jar &

# Wait 10 seconds, then...

# 2. API Gateway
java -jar api-gateway/target/gateway-service-0.0.1-SNAPSHOT.jar &

# 3. Auth Service
java -jar auth-service/target/auth-service-0.0.1-SNAPSHOT.jar &

# 4. Client Service
java -jar client-service/target/client-service-0.0.1-SNAPSHOT.jar &

# 5. Product Service
java -jar produit/target/produit-service-0.0.1-SNAPSHOT.jar &

# 6. Order Service
java -jar commande-service/target/commande-service-0.0.1-SNAPSHOT.jar &

# 7. AI Agent Service (requires Ollama running)
java -jar agent-ia-service/target/agent-ia-service-0.0.1-SNAPSHOT.jar &
```

### Using the Start Script

```bash
chmod +x start-all.sh
./start-all.sh
```

---

## 16. API Testing

### Auth Service

```bash
# Create a role
curl -X POST -H "Content-Type: application/json" \
  -d '{"roleName":"ADMIN"}' \
  http://localhost:8080/roles

# Create a user
curl -X POST -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}' \
  http://localhost:8080/users

# Assign role to user
curl -X POST -H "Content-Type: application/json" \
  -d '{"username":"admin","roleName":"ADMIN"}' \
  http://localhost:8080/addRoleToUser

# Login and get JWT token
curl -X POST -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}' \
  http://localhost:8080/login

# Response: {"access-token":"eyJ..."}
```

### Through API Gateway (Authenticated)

```bash
# Set your token
TOKEN="eyJ..."

# Get clients
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8888/client-service/clients

# Get products
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8888/produit-service/produits

# Get orders
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8888/commande-service/commandes
```

### AI Chat

```bash
# Chat as admin
curl -H "Authorization: Bearer $TOKEN" \
  "http://localhost:8081/chat?message=Show%20me%20all%20products"

# Chat without token (limited access)
curl "http://localhost:8081/chat?message=List%20products"
```

### Eureka Dashboard

Open http://localhost:8761 to see all registered services.

---

## 17. Frontend Integration

The project includes an Angular frontend located in the `frontend/` directory.

### Setup

```bash
cd frontend
npm install
ng serve
```

### Access
- **URL**: http://localhost:4200

### Key Features
- Product listing
- Client management
- Order visualization
- AI Chat interface

---

## 📝 Summary

This microservices project demonstrates:

1. **Service Discovery** with Netflix Eureka
2. **API Gateway** pattern with Spring Cloud Gateway
3. **JWT Security** with role-based access control
4. **Inter-service Communication** using OpenFeign
5. **AI Integration** with Spring AI and MCP
6. **Clean Architecture** with separation of concerns

### Service Ports Quick Reference

| Service | Port |
|---------|------|
| Eureka | 8761 |
| Gateway | 8888 |
| Auth | 8080 |
| Client | 8082 |
| Product | 9081 |
| Order | 9092 |
| AI Agent | 8081 |

---

## 🔗 Resources

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- [Spring Cloud Documentation](https://spring.io/projects/spring-cloud)
- [Spring AI Documentation](https://docs.spring.io/spring-ai/reference/)
- [MCP Specification](https://modelcontextprotocol.io/)
- [JWT.io](https://jwt.io/)

---

*Last Updated: January 2026*
