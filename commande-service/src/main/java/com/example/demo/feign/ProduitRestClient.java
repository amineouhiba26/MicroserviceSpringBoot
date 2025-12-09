package com.example.demo.feign;

import com.example.demo.model.Produit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "PRODUIT-SERVICE")
public interface ProduitRestClient {
    @GetMapping("/produits/{id}")
    Produit findProduitById(@PathVariable Long id);

    @GetMapping("/produits")
    List<Produit> getProduits();
}

