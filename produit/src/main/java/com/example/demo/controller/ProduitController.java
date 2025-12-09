package com.example.demo.controller;

import com.example.demo.produits.Produit;
import com.example.demo.repository.ProduitRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController

public class ProduitController {
    private ProduitRepository produitRepository;
    @GetMapping("/produits")
    public List<Produit> all() {
        return produitRepository.findAll();
    }
    @GetMapping("/produits/{id}")
    public Produit get(@PathVariable long id) {
        return produitRepository.findById(id).get();
    }
}
