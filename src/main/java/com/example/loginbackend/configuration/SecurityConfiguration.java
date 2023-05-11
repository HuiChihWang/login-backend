package com.example.loginbackend.configuration;

import com.example.loginbackend.entity.AppUserRole;
import lombok.AllArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class SecurityConfiguration {

    private final AuthenticationProvider authProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(req -> req
                        .requestMatchers(antMatcher("/public/**")).permitAll()
                        .requestMatchers(antMatcher("/api/v*/register/**")).permitAll()
                        .requestMatchers(antMatcher("/api/v*/admin")).hasRole(AppUserRole.ADMIN.name())
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authProvider)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin();

        return http.build();
    }
}
