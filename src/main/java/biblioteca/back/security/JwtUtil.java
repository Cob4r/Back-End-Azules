package biblioteca.back.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key key;
    private final long expirationMs;

    public JwtUtil(@Value("${jwt.secret}") String secret,
                @Value("${jwt.expirationMs}") long expirationMs) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationMs = expirationMs;
    }

    // ⭐ CREAR TOKEN
    public String generateToken(String username) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // ⭐ OBTENER USERNAME
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // ⭐ VALIDAR TOKEN (CON LOGS)
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            System.out.println("✔ TOKEN VALIDO");
            return true;

        } catch (ExpiredJwtException e) {
            System.out.println("❌ TOKEN EXPIRADO: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("❌ TOKEN NO SOPORTADO: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.println("❌ TOKEN MAL FORMADO: " + e.getMessage());
        } catch (SignatureException e) {
            System.out.println("❌ FIRMA INVALIDA: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("❌ TOKEN VACIO O NULO: " + e.getMessage());
        }

        return false;
    }
}
