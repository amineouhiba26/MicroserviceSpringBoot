package com.example.auth.security;

import com.example.auth.repo.AppUserRepository;
import com.example.auth.entities.AppUser;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@AllArgsConstructor
public class BeanConfig {

    private final AppUserRepository userRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        // On retourne une implementation de l'interface fonctionnelle
        return username -> {
            // 1. Chercher l'utilisateur dans notre base de donnees
            AppUser appUser = userRepository.findByUsername(username);
            if (appUser == null) {
                // Si l'utilisateur n'est pas trouve, on doit lever cette exception
                throw new UsernameNotFoundException("Utilisateur non trouve");
            }

            // 2. Transformer notre AppUser en UserDetails de Spring Security
            return new User(
                    appUser.getUsername(),
                    appUser.getPassword(),
                    // On transforme notre liste de AppRole en une collection de GrantedAuthority
                    // C'est le format que Spring Security comprend pour les roles.
                    appUser.getRoles().stream()
                            .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                            .toList()
            );
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // On utilise BCrypt, l'algorithme standard de l'industrie pour le hachage
        // Il est securise car il est adaptatif et integre un "sel" (salt) automatiquement.
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        // DaoAuthenticationProvider est l'implementation standard pour l'authentification
        // basee sur une base de donnees.
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        // On lui fournit notre UserDetailsService pour qu'il sache ou trouver les utilisateurs.
        authProvider.setUserDetailsService(userDetailsService());

        // On lui fournit notre PasswordEncoder pour qu'il sache comment verifier les mots de passe.
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        // Cette methode, fournie par Spring, expose le gestionnaire d'authentification
        // configure par defaut, qui utilisera notre AuthenticationProvider.
        return config.getAuthenticationManager();
    }
}
