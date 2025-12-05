package biblioteca.back.service;

import biblioteca.back.entity.Usuario;
import biblioteca.back.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registrarUsuario_correcto() {
        String username = "benja";
        String password = "1234";

        when(usuarioRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn("encrypted");

        Usuario usuario = Usuario.builder()
                .id(1L)
                .username(username)
                .password("encrypted")
                .rol("USER")
                .build();

        when(usuarioRepository.save(any())).thenReturn(usuario);

        Usuario resultado = usuarioService.registrarUsuario(username, password);

        assertNotNull(resultado);
        assertEquals(username, resultado.getUsername());
        assertEquals("encrypted", resultado.getPassword());
    }

    @Test
    void registrarUsuario_yaExiste() {
        when(usuarioRepository.findByUsername("benja"))
                .thenReturn(Optional.of(new Usuario()));

        assertThrows(IllegalArgumentException.class, () ->
                usuarioService.registrarUsuario("benja", "1234")
        );
    }
}
