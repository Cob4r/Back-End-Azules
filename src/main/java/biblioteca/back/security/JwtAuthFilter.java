package biblioteca.back.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();
        System.out.println("➡ PATH RECIBIDO: " + path);

        // ⭐ 1. Ignorar rutas públicas
        if (path.startsWith("/api/auth/")) {
            System.out.println("➡ RUTA PÚBLICA — JWT IGNORADO");
            filterChain.doFilter(request, response);
            return;
        }

        // ⭐ 2. Leer Header Authorization
        String header = request.getHeader("Authorization");
        System.out.println("➡ HEADER RECIBIDO: " + header);
        
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        // ⭐ 3. Validar token
        if (!jwtUtil.validateToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // ⭐ 4. Extraer username del token
        String username = jwtUtil.getUsernameFromToken(token);

        // ⭐ 5. Si no hay autenticación previa, autenticar
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );

            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        // ⭐ 6. Continuar la cadena de filtros
        filterChain.doFilter(request, response);
    }
}
