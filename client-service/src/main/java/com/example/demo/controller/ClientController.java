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
