package com.example.auth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Service pour la gestion des JSON Web Tokens (JWT).
 * Cette classe centralise toutes les operations liees aux JWT :
 * - Generation du token a partir des informations de l'utilisateur.
 * - Extraction des informations (claims) contenues dans le token.
 * - Validation de la conformite et de l'integrite du token.
 * L'annotation @Service indique a Spring que cette classe est un composant de la couche de service.
 */
@Service
public class JwtService {

    // Cle secrete utilisee pour signer et verifier les tokens.
    // Elle est injectee depuis les proprietes de l'application (ex: application.properties).
    // Il est crucial que cette cle soit longue, complexe et gardee secrete.
    @Value("${spring.app.secretkey}")
    private String secretKey;

    // Duree de validite du token en millisecondes.
    // Egalement injectee depuis les proprietes de l'application.
    @Value("${spring.app.expiration}")
    private long expiration;

    /**
     * Extrait le nom d'utilisateur (le "sujet" du token) a partir d'un token JWT.
     * Le sujet est une des revendications (claims) standard d'un JWT et identifie de maniere unique l'entite concernee par le token.
     *
     * @param token Le JWT sous forme de chaine de caracteres.
     * @return Le nom d'utilisateur extrait du token.
     */
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Genere un nouveau token JWT pour un utilisateur authentifie.
     * Cette methode est typiquement appelee apres une connexion reussie.
     *
     * @param userDetails Les details de l'utilisateur (fournis par Spring Security) pour qui le token doit etre genere.
     * @return Une chaine de caracteres representant le JWT compacte et signe.
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        // Ajout des roles (autorites) de l'utilisateur comme une revendication personnalisee ("claim").
        // Cela permet de creer un token "autonome" : le serveur peut verifier les autorisations
        // de l'utilisateur en se basant uniquement sur le contenu du token, sans avoir a interroger
        // la base de donnees a chaque requete. C'est une optimisation de performance et de conception.
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList());

        return createToken(claims, userDetails.getUsername());
    }

    /**
     * Valide un token JWT.
     * La validation se fait en deux etapes cruciales pour la securite :
     * 1. On verifie que le nom d'utilisateur dans le token correspond a celui de l'utilisateur en cours de traitement.
     * 2. On s'assure que le token n'a pas expire.
     *
     * @param token Le JWT a valider.
     * @param userDetails Les details de l'utilisateur a comparer avec les informations du token.
     * @return true si le token est valide, false sinon.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // --- Methodes privees utilitaires ---

    /**
     * Verifie si un token a expire en comparant sa date d'expiration avec la date actuelle.
     *
     * @param token Le JWT a verifier.
     * @return true si le token a expire, false sinon.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extrait la date d'expiration d'un token JWT.
     *
     * @param token Le JWT.
     * @return La date d'expiration (java.util.Date).
     */
    private Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    /**
     * Methode centrale pour la creation du token.
     * Elle utilise un "builder" pour assembler les differentes parties du JWT :
     * - Les revendications (claims), incluant les claims personnalises et le sujet.
     * - La date d'emission (Issued At).
     * - La date d'expiration (Expiration).
     * - La signature avec l'algorithme et la cle secrete.
     *
     * @param claims Les revendications (payload) a inclure dans le token.
     * @param subject Le sujet du token (generalement le nom d'utilisateur).
     * @return Le JWT final, serialise sous forme de chaine compacte.
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey()) // Signature du token
                .compact(); // Construction et serialisation du token
    }

    /**
     * Extrait toutes les revendications (claims) d'un token.
     * C'est une operation critique : avant d'extraire le corps (payload), la bibliotheque `jjwt`
     * va d'abord verifier la signature du token en utilisant la cle secrete.
     * Si la signature est invalide (le token a ete altere), une exception sera levee,
     * garantissant ainsi l'integrite des donnees.
     *
     * @param token Le JWT a parser.
     * @return Un objet Claims contenant toutes les informations du payload.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Genere la cle de signature a partir de la cle secrete encodee en Base64.
     * Cette methode decode d'abord la cle secrete, puis cree une instance de `Key`
     * specifique a l'algorithme HMAC-SHA.
     *
     * @return L'objet Key pret a etre utilise pour la signature/verification.
     */
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
