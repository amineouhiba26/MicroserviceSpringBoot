package com.example.auth.web;

import com.example.auth.dto.AuthenticationRequest;
import com.example.auth.security.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @PostMapping("/login")
    public String login(@RequestBody AuthenticationRequest request) {
        // Valide les identifiants en utilisant le manager de Spring Security
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // Si l'authentification reussit, on charge les details de l'utilisateur
        final UserDetails user = userDetailsService.loadUserByUsername(request.getUsername());

        // On genere et retourne le token JWT
        return jwtService.generateToken(user);
    }
}
