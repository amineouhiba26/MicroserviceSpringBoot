package com.example.demo.controller;

import com.example.demo.dto.CommandeDTO;
import com.example.demo.dto.ProductItemDTO;
import com.example.demo.entities.Commande;
import com.example.demo.entities.ProductItem;
import com.example.demo.feign.ClientRestClient;
import com.example.demo.feign.ProduitRestClient;
import com.example.demo.model.Client;
import com.example.demo.model.Produit;
import com.example.demo.repository.CommandeRepository;
import com.example.demo.repository.ProductItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;

@RestController
@AllArgsConstructor
public class CommandeController {
    private CommandeRepository commandeRepository;
    private ProductItemRepository productItemRepository;
    private ClientRestClient clientRestClient;
    private ProduitRestClient produitRestClient;

    @McpTool(name = "getOrders", description = "Récupère la liste de toutes les commandes")
    @GetMapping("/commandes")
    public List<Commande> all() {
        return commandeRepository.findAll();
    }

    @McpTool(name = "getOrderDetails", description = "Récupère les détails d'une commande (client + produits) via son ID")
    @GetMapping("/commandes/{id}")
    public Commande getCommandeDetails(@McpToolParam(description = "ID de la commande") @PathVariable Long id) {
        Commande commande = commandeRepository.findById(id).orElse(null);
        if (commande == null) return null;

        Client client = clientRestClient.findClientById(commande.getIdClient());
        commande.setClient(client);

        commande.getProductItems().forEach(pi -> {
            Produit produit = produitRestClient.findProduitById(pi.getIdProduit());
            pi.setProduit(produit);
        });

        return commande;
    }
    
    @PostMapping("/commandes")
    public Commande create(@RequestBody CommandeDTO commandeDTO) {
        Commande commande = new Commande();
        commande.setIdClient(commandeDTO.getIdClient());
        commande.setDate(new Date());
        Commande savedCommande = commandeRepository.save(commande);
        
        for (ProductItemDTO itemDTO : commandeDTO.getProductItems()) {
            Produit produit = produitRestClient.findProduitById(itemDTO.getIdProduit());
            
            ProductItem productItem = new ProductItem();
            productItem.setIdProduit(itemDTO.getIdProduit());
            productItem.setQuantite(itemDTO.getQuantite());
            productItem.setPrix(produit.getPrix());
            productItem.setCommande(savedCommande);
            productItemRepository.save(productItem);
        }
        
        savedCommande = commandeRepository.findById(savedCommande.getId()).orElse(null);
        if (savedCommande != null) {
            Client client = clientRestClient.findClientById(savedCommande.getIdClient());
            savedCommande.setClient(client);
            
            if (savedCommande.getProductItems() != null) {
                savedCommande.getProductItems().forEach(pi -> {
                    Produit produit = produitRestClient.findProduitById(pi.getIdProduit());
                    pi.setProduit(produit);
                });
            }
        }
        
        return savedCommande;
    }
}
