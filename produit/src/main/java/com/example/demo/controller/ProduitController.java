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
        System.out.println("Produit supprimé avec l'ID : " + id);
    }
}
