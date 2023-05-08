package com.example.loginbackend.service;

import com.example.loginbackend.entity.AppUser;
import com.example.loginbackend.entity.AppUserRole;
import com.example.loginbackend.entity.ConfirmationToken;
import com.example.loginbackend.exception.UserAlreadyExistedException;
import com.example.loginbackend.repository.AppUserRepository;
import com.example.loginbackend.request.RegistrationRequest;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private static final Logger logger = LoggerFactory.getLogger(RegistrationService.class);
    @NonNull
    private final AppUserRepository userRepository;

    @NonNull
    private final PasswordEncoder passwordEncoder;

    @NonNull
    private final ConfirmationTokenService tokenService;

    public AppUser registration(RegistrationRequest request) {
        Optional<AppUser> userWithUsername = userRepository.findAppUserByUsername(request.username());
        if (userWithUsername.isPresent()) {
            throw new UserAlreadyExistedException(userWithUsername.get(), UserAlreadyExistedException.ERROR_TYPE.USERNAME_EXISTED);
        }

        Optional<AppUser> userWithEmail = userRepository.findAppUserByEmail(request.email());
        if (userWithEmail.isPresent()) {
            throw new UserAlreadyExistedException(userWithEmail.get(), UserAlreadyExistedException.ERROR_TYPE.EMAIL_EXISTED);
        }

        String encryptPassword = passwordEncoder.encode(request.password());
        AppUser newUser = new AppUser(
                request.username(),
                request.email(),
                request.name(),
                encryptPassword,
                AppUserRole.USER
        );

        AppUser savedUser = userRepository.save(newUser);

        // TODO: call token service to generate token
        ConfirmationToken token = tokenService.generateConfirmationToken(savedUser);
        logger.info("Register new user {} with confirmation token {}", savedUser.getId(), token.getToken());

        // TODO: sending confirmation Email
        return savedUser;
    }
}
