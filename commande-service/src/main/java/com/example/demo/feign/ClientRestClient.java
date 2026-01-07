package com.example.demo.feign;

import com.example.demo.model.Client;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@FeignClient(name = "CLIENT-SERVICE")
public interface ClientRestClient {

    @GetMapping("/clients/{id}")
    @CircuitBreaker(name = "client-service-cb", fallbackMethod = "getDefaultClient")
    Client findClientById(@PathVariable Long id);

    @GetMapping("/clients")
    @CircuitBreaker(name = "client-service-cb", fallbackMethod = "getDefaultClients")
    List<Client> getClients();



    default Client getDefaultClient(Long id, Exception exception) {
        Client client = new Client();
        client.setId(id);
        client.setNom("Client Non Disponible");
        client.setEmail("defaut@error.com");
        return client;
    }

    default List<Client> getDefaultClients(Exception exception) {
        return new ArrayList<>();
    }
}

