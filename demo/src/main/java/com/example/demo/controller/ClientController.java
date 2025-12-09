package com.example.demo.controller;

import com.example.demo.clients.Client;
import com.example.demo.repository.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
public class ClientController {
    private ClientRepository clientRepository;
    
    @GetMapping("/clients")
    public List<Client> all() {
        return clientRepository.findAll();
    }
    
    @GetMapping("/clients/{id}")
    public Client get(@PathVariable Integer id) {
        return clientRepository.findById(id).get();
    }
    
    @PostMapping("/clients")
    public Client create(@RequestBody Client client) {
        return clientRepository.save(client);
    }
}
