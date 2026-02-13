package com.example.backend_security.security;

import com.example.backend_security.constants.AuthConstants;
import com.example.backend_security.exception.JwtAuthenticationException;
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
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;



    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getServletPath();

        if (path.startsWith("/security/api/v1/v3/api-docs")
                || path.startsWith("/swagger-ui")

                || path.equals("/users")) {
            filterChain.doFilter(request, response);
            return;
        }


        String requestTokenHeader = request.getHeader("Authorization");
        String jwtToken = null;
        String usernameOrEmail = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                usernameOrEmail = this.jwtUtil.extractUsername(jwtToken);
            } catch (Exception e) {
                throw new JwtAuthenticationException(AuthConstants.TOKEN_INVALIDO);
            }
        }

        if (usernameOrEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(usernameOrEmail);

                if (this.jwtUtil.validateToken(jwtToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities()
                            );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    throw new JwtAuthenticationException(AuthConstants.TOKEN_NO_VALIDO_PARA_USUARIO);
                }
            } catch (Exception ex) {
                throw new JwtAuthenticationException(AuthConstants.ERROR_VALIDANDO_TOKEN);
            }
        }

        filterChain.doFilter(request, response);
    }
}