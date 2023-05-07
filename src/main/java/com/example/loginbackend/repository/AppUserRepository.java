package com.example.loginbackend.repository;

import com.example.loginbackend.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findAppUserByEmail(String email);
    Optional<AppUser> findAppUserByUsername(String username);

    boolean existsAppUserByEmail(String email);
    boolean existsAppUserByUsername(String username);
}
