package com.example.loginbackend.configuration;

import com.example.loginbackend.utility.JwtUtility;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private static final String AUTH_HEADER_NAME = "Authorization";
    private static final String USERNAME_ATTRIBUTE = "username";

    private final JwtUtility jwtUtility;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader(AUTH_HEADER_NAME);
        String usernameInRequest = (String) request.getAttribute(USERNAME_ATTRIBUTE);

        String token = jwtUtility.getJwtTokenFromAuthHeader(authHeader);

        if (token == null || usernameInRequest == null) {
            filterChain.doFilter(request, response);
            return;
        }

        UserDetails userInRequest = userDetailsService.loadUserByUsername(usernameInRequest);

        if (!jwtUtility.isTokenValid(token, userInRequest)) {
            filterChain.doFilter(request, response);
            return;
        }

        SecurityContext authHolder = SecurityContextHolder.getContext();

        if (authHolder.getAuthentication() == null) {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userInRequest, null, userInRequest.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }
}
