package biblioteca.back.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    @Test
    void generarYValidarToken() {
        JwtUtil jwt = new JwtUtil(
                "12345678901234567890123456789012",
                3600000
        );

        String token = jwt.generateToken("benja");

        assertTrue(jwt.validateToken(token));
        assertEquals("benja", jwt.getUsernameFromToken(token));
    }
}
