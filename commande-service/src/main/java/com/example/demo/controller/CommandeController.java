package com.example.demo.controller;

import com.example.demo.entities.Commande;
import com.example.demo.feign.ClientRestClient;
import com.example.demo.feign.ProduitRestClient;
import com.example.demo.model.Client;
import com.example.demo.model.Produit;
import com.example.demo.repository.CommandeRepository;
import com.example.demo.repository.ProductItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class CommandeController {
    private CommandeRepository commandeRepository;
    private ProductItemRepository productItemRepository;
    private ClientRestClient clientRestClient;
    private ProduitRestClient produitRestClient;

    @GetMapping("/commandes")
    public List<Commande> all() {
        return commandeRepository.findAll();
    }

    @GetMapping("/commandes/{id}")
    public Commande getCommandeDetails(@PathVariable Long id) {
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
}
