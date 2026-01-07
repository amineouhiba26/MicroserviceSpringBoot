package com.example.demo;

import com.example.demo.entities.Commande;
import com.example.demo.entities.ProductItem;
import com.example.demo.feign.ClientRestClient;
import com.example.demo.feign.ProduitRestClient;
import com.example.demo.model.Client;
import com.example.demo.model.Produit;
import com.example.demo.repository.CommandeRepository;
import com.example.demo.repository.ProductItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;

@EnableFeignClients 
@SpringBootApplication
public class CommandeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommandeServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(
			CommandeRepository commandeRepository,
			ProductItemRepository productItemRepository,
			ProduitRestClient productRestClient,
			ClientRestClient clientRestClient
	) {
		return args -> {
			
		};
	}
}
