package com.example.loginbackend.service;

import com.example.loginbackend.entity.AppUser;
import com.example.loginbackend.entity.AppUserRole;
import com.example.loginbackend.entity.ConfirmationToken;
import com.example.loginbackend.exception.TokenExpiredException;
import com.example.loginbackend.exception.TokenNotExistedException;
import com.example.loginbackend.exception.UserAlreadyExistedException;
import com.example.loginbackend.repository.AppUserRepository;
import com.example.loginbackend.request.RegistrationRequest;

import com.example.loginbackend.utility.EmailTemplate;
import com.example.loginbackend.utility.LinkUtility;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationService.class);
    @NonNull
    private final AppUserRepository userRepository;

    @NonNull
    private final PasswordEncoder passwordEncoder;

    @NonNull
    private final EmailService emailService;

    @NonNull
    private final EmailTemplate emailTemplate;

    @NonNull
    private final ConfirmationTokenService tokenService;

    @Transactional
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
                AppUserRole.ROLE_USER
        );

        AppUser savedUser = userRepository.save(newUser);

        ConfirmationToken token = tokenService.generateConfirmationToken(savedUser);
        LOGGER.info("Register new user {} with confirmation token {}", savedUser.getId(), token.getToken());

        String confirmationLink = LinkUtility.buildConfirmationLink(token.getToken());
        String mailContent = emailTemplate.buildConfirmationEmail(savedUser.getName(), confirmationLink);
        emailService.sendEmail(
                savedUser.getEmail(),
                String.format(
                        "Hi %s, Please Activate Your Newly Registered Account!",
                        savedUser.getUsername()
                ),
                mailContent
        );
        return savedUser;
    }

    public String activateUserByConfirmationToken(String strToken) {
        try {
            tokenService.activateConfirmationToken(strToken);
        } catch (TokenExpiredException | TokenNotExistedException activationException) {
            return activationException.getMessage();
        }
        return "";
    }

    // TODO: add resending confirmation mail service
    public void resendConfirmationEmail(AppUser user) {
        ConfirmationToken newGeneratedToken = tokenService.generateConfirmationToken(user);
    }
}
