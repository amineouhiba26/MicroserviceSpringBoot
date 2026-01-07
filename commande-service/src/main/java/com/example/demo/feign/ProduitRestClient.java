package com.example.demo.feign;

import com.example.demo.model.Produit;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@FeignClient(name = "PRODUIT-SERVICE")
public interface ProduitRestClient {

    @GetMapping("/produits/{id}")
    @CircuitBreaker(name = "produit-service-cb", fallbackMethod = "getDefaultProduit")
    Produit findProduitById(@PathVariable Long id);

    @GetMapping("/produits")
    @CircuitBreaker(name = "produit-service-cb", fallbackMethod = "getDefaultProduits")
    List<Produit> getProduits();


    default Produit getDefaultProduit(Long id, Exception exception) {
        Produit produit = new Produit();
        produit.setId(id);
        produit.setNom("Produit Non Disponible");
        produit.setPrix(0.0);
        return produit;
    }

    default List<Produit> getDefaultProduits(Exception exception) {
        return new ArrayList<>();
    }
}

