package com.example.loginbackend.configuration;

import com.example.loginbackend.utility.JwtUtility;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String AUTH_HEADER_NAME = "Authorization";

    @NonNull
    private final JwtUtility jwtUtility;

    @NonNull
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader(AUTH_HEADER_NAME);
        String token = jwtUtility.getJwtTokenFromAuthHeader(authHeader);

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String userNameInToken = jwtUtility.getUsernameFromToken(token);

        if (userNameInToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        UserDetails userInToken = userDetailsService.loadUserByUsername(userNameInToken);

        if (!jwtUtility.isTokenValid(token, userInToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        SecurityContext authHolder = SecurityContextHolder.getContext();

        if (authHolder.getAuthentication() == null) {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userInToken, null, userInToken.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }
}
