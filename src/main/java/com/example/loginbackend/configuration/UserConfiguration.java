package com.example.loginbackend.configuration;

import com.example.loginbackend.entity.AppUser;
import com.example.loginbackend.entity.AppUserRole;
import com.example.loginbackend.repository.AppUserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class UserConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserConfiguration.class);

    @NonNull
    private final PasswordEncoder encoder;

    @NonNull
    private final AppUserRepository userRepository;

    @Bean
    CommandLineRunner createAdmin() {
        String encodedPwd = encoder.encode("admin");
        AppUser user = AppUser.builder()
                .isEnabled(true)
                .isLocked(false)
                .username("admin")
                .password(encodedPwd)
                .email("taya30621@gmail.com")
                .name("Hui Chih Wang")
                .role(AppUserRole.ROLE_ADMIN)
                .build();

        AppUser admin = userRepository.save(user);

        return args -> LOGGER.info("Create admin user {}", admin);
    }

    @Bean
    CommandLineRunner createUser() {
        String encodedPwd = encoder.encode("@Taya30621");
        AppUser user = AppUser.builder()
                .isEnabled(true)
                .isLocked(false)
                .username("taya87136")
                .password(encodedPwd)
                .email("taya87136@gmail.com")
                .name("Gilbert Wang")
                .role(AppUserRole.ROLE_USER)
                .build();

        AppUser newUser = userRepository.save(user);

        return args -> LOGGER.info("Create new user {}", newUser);
    }

}
