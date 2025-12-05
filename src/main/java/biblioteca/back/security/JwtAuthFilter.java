package biblioteca.back.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
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

        String path = request.getRequestURI();
        System.out.println("➡ PATH RECIBIDO: " + path);

        // ⭐ Rutas públicas - no se valida token ⭐
        if (path.equals("/api/auth/login") || path.equals("/api/auth/register")) {
            System.out.println("➡ RUTA PÚBLICA — JWT IGNORADO");
            filterChain.doFilter(request, response);
            return;
        }

        // ⭐ Leer Authorization
        String header = request.getHeader("Authorization");
        System.out.println("➡ HEADER RECIBIDO: " + header);

        if (header == null || !header.startsWith("Bearer ")) {
            System.out.println("⛔ SIN TOKEN EN RUTA PROTEGIDA");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String token = header.substring(7);

        // ⭐ Validar token
        if (!jwtUtil.validateToken(token)) {
            System.out.println("⛔ TOKEN INVALIDO");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        // ⭐ Obtener usuario del token
        String username = jwtUtil.getUsernameFromToken(token);

        // ⭐ Autenticar si no hay sesión previa
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

            authenticationToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        filterChain.doFilter(request, response);
    }
}
