package com.example.demo.controller;

import com.example.demo.entities.Produit;
import com.example.demo.repository.ProduitRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
public class ProduitController {
    private ProduitRepository produitRepository;
    
    @GetMapping("/produits" )
    public List<Produit> all() {
        return produitRepository.findAll();
    }
    
    @GetMapping("/produits/{id}")
    public Produit get(@PathVariable long id) {
        return produitRepository.findById(id).get();
    }
    
    @PostMapping("/produits")
    public Produit create(@RequestBody Produit produit) {
        return produitRepository.save(produit);
    }
}
