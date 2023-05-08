package com.example.loginbackend.repository;

import com.example.loginbackend.entity.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    boolean existsByToken(String token);
    Optional<ConfirmationToken> findConfirmationTokenByToken(String token);
}
