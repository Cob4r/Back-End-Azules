package biblioteca.back.controller;

import biblioteca.back.entity.Usuario;
import biblioteca.back.security.JwtUtil;
import biblioteca.back.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private AuthenticationManager authManager;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registrarUsuario_ok() {
        Usuario usuario = Usuario.builder()
                .id(1L)
                .username("benja")
                .password("1234")
                .rol("USER")
                .build();

        when(usuarioService.registrarUsuario("benja", "1234")).thenReturn(usuario);

        var req = new AuthController.RegisterRequest();
        req.setUsername("benja");
        req.setPassword("1234");

        ResponseEntity<?> resp = authController.register(req);

        assertTrue(resp.getBody().toString().contains("1"));
    }

    @Test
    void login_ok() {
        String token = "aaaa.bbbb.cccc";

        AuthController.LoginRequest req = new AuthController.LoginRequest();
        req.setUsername("benja");
        req.setPassword("1234");

        when(jwtUtil.generateToken("benja")).thenReturn(token);

        ResponseEntity<?> response = authController.login(req);

        assertEquals(token, ((AuthController.JwtResponse) response.getBody()).getToken());
    }
}
