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

