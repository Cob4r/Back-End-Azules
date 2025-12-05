package biblioteca.back.security;

import biblioteca.back.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        String role = usuario.getRol() != null ? usuario.getRol() : "USER";

        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .roles(role) // → genera ROLE_USER automáticamente
                .build();
    }
}
