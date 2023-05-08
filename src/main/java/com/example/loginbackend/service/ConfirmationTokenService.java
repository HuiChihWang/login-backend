package com.example.loginbackend.service;

import com.example.loginbackend.entity.AppUser;
import com.example.loginbackend.entity.ConfirmationToken;
import com.example.loginbackend.exception.TokenGenerationFailException;
import com.example.loginbackend.repository.ConfirmationTokenRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenService {
    private static final int TOKEN_LIFE_MINUTES = 20;
    private static final int MAXIMUM_GENERATE_TIMES = 10;

    @NonNull
    private final ConfirmationTokenRepository tokenRepository;

    public ConfirmationToken generateConfirmationToken(AppUser user) {
        // TODO: check if there is non-activated token of user or non-expired token

        // create token for user if there is no
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiredTime = now.plusMinutes(TOKEN_LIFE_MINUTES);
        ConfirmationToken token = new ConfirmationToken(
                generateNewTokenString(),
                now,
                expiredTime,
                user
        );

        return tokenRepository.save(token);
    }

    private String generateNewTokenString() {
        String token = "";
        int generateTimes = 0;

        while(generateTimes == 0 || isTokenExist(token)) {
            if (generateTimes >= MAXIMUM_GENERATE_TIMES) {
                throw new TokenGenerationFailException("Cannot generate confirmation token");
            }
            token = UUID.randomUUID().toString();
            ++generateTimes;
        }

        return token;
    }

    private boolean isTokenExist(String token) {
        return tokenRepository.existsByToken(token);
    }
}
