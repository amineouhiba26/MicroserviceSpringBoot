package com.example.demo.controller;

import com.example.demo.entities.Client;
import com.example.demo.repository.ClientRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ClientController {
    private ClientRepository clientRepository;

    public ClientController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @GetMapping("/clients")
    public List<Client> clients() {
        return clientRepository.findAll();
    }

    @GetMapping("/clients/{id}")
    public Client client(@PathVariable Long id) {
        return clientRepository.findById(id).orElse(null);
    }
}
