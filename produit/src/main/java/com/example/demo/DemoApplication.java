package com.example.demo;

import com.example.demo.produits.Produit;
import com.example.demo.repository.ProduitRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	//@Bean
	CommandLineRunner initProduits(ProduitRepository produitRepository) {
		return args -> {
			if (produitRepository.count() == 0) {
				produitRepository.save(new Produit(0, "Ordinateur Portable", 899.99));
				produitRepository.save(new Produit(0, "Souris Sans Fil", 29.99));
				produitRepository.save(new Produit(0, "Clavier Mécanique", 149.99));
				produitRepository.save(new Produit(0, "Écran 27 pouces", 299.99));
				produitRepository.save(new Produit(0, "Casque Audio", 79.99));
				produitRepository.save(new Produit(0, "Webcam HD", 59.99));
				produitRepository.save(new Produit(0, "USB 64GB", 19.99));
				produitRepository.save(new Produit(0, "Disque Dur 1TB", 89.99));
				
				System.out.println("✅ " + produitRepository.count() + " produits créés avec succès !");
			} else {
				System.out.println("ℹ️ " + produitRepository.count() + " produits déjà existants.");
			}
		};
	}

}
