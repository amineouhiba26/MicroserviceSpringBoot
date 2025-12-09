package com.example.demo;

import com.example.demo.clients.Client;
import com.example.demo.repository.ClientRepository;
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
	CommandLineRunner initClients(ClientRepository clientRepository) {
		return args -> {
			// Vérifier si des clients existent déjà
			if (clientRepository.count() == 0) {
				// Créer des clients de test
				clientRepository.save(new Client(0, "Alice Dupont", "alice.dupont@email.com"));
				clientRepository.save(new Client(0, "Bob Martin", "bob.martin@email.com"));
				clientRepository.save(new Client(0, "Charlie Bernard", "charlie.bernard@email.com"));
				clientRepository.save(new Client(0, "Diana Rousseau", "diana.rousseau@email.com"));
				clientRepository.save(new Client(0, "Eric Lefevre", "eric.lefevre@email.com"));
				
				System.out.println("✅ " + clientRepository.count() + " clients créés avec succès !");
			} else {
				System.out.println("ℹ️ " + clientRepository.count() + " clients déjà existants.");
			}
		};
	}

}
