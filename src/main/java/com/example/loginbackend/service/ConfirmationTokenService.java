package com.example.loginbackend.service;

import com.example.loginbackend.entity.AppUser;
import com.example.loginbackend.entity.ConfirmationToken;
import com.example.loginbackend.exception.TokenExpiredException;
import com.example.loginbackend.exception.TokenGenerationFailException;
import com.example.loginbackend.exception.TokenNotExistedException;
import com.example.loginbackend.repository.ConfirmationTokenRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenService {
    private static final int TOKEN_LIFE_MINUTES = 20;
    private static final int MAXIMUM_GENERATE_TIMES = 10;

    @NonNull
    private final ConfirmationTokenRepository tokenRepository;

    @Transactional
    public ConfirmationToken generateConfirmationToken(AppUser user) {
        if (user.isEnabled()) {
            throw new TokenGenerationFailException(String.format("User with id %d has been activated", user.getId()));
        }

        ConfirmationToken latestToken = getLatestToken(user);
        if (latestToken != null) {
            latestToken.setExpiredAt(LocalDateTime.now());
            tokenRepository.save(latestToken);
        }

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

    public ConfirmationToken getConfirmationToken(String token) {
        return tokenRepository
                .findConfirmationTokenByToken(token)
                .orElseThrow(() -> new TokenNotExistedException(
                        String.format("Token %s does not exist.", token)
                ));
    }

    @Transactional
    public void activateConfirmationToken(String strToken) {
        ConfirmationToken token = getConfirmationToken(strToken);

        if (isTokenUsed(strToken)) {
            throw new TokenExpiredException("User has already been activated.");
        }

        if (isTokenExpired(strToken)) {
            throw new TokenExpiredException(
                    String.format(
                            "The link was expired at %s",
                            token.getExpiredAt().toString())
            );
        }

        token.setConfirmedAt(LocalDateTime.now());

        AppUser user = token.getUser();
        user.setEnabled(true);

        tokenRepository.save(token);
    }

    private String generateNewTokenString() {
        String token = "";
        int generateTimes = 0;

        while (generateTimes == 0 || isTokenExist(token)) {
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

    private boolean isTokenExpired(String strToken) {
        ConfirmationToken token = getConfirmationToken(strToken);
        return LocalDateTime.now().isAfter(token.getExpiredAt());
    }

    private boolean isTokenUsed(String strToken) {
        ConfirmationToken token = getConfirmationToken(strToken);
        AppUser user = token.getUser();
        return user.isEnabled();
    }
    private ConfirmationToken getLatestToken(AppUser user) {
        List<ConfirmationToken> tokens = user.getTokens();

        if (tokens.isEmpty()) {
            return null;
        }

        return Collections.max(tokens, (token1, token2) -> {
            LocalDateTime creationDate1 = token1.getCreatedAt();
            LocalDateTime creationDate2 = token2.getCreatedAt();
           return creationDate1.compareTo(creationDate2);
        });
    }
}
