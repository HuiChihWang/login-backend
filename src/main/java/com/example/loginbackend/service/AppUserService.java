package com.example.loginbackend.service;

import com.example.loginbackend.dto.UserDTO;
import com.example.loginbackend.dto.UserDTOMapper;
import com.example.loginbackend.entity.AppUser;
import com.example.loginbackend.exception.UserNotFoundException;
import com.example.loginbackend.repository.AppUserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppUserService implements UserDetailsService {
    @NonNull
    private final AppUserRepository appUserRepository;

    @NonNull
    private final UserDTOMapper userDTOMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserRepository
                .findAppUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("User with username %s doesn't exist.\n", username)
                ));
    }

    public UserDTO getUserInfoById(long userId) {
        Optional<AppUser> user = appUserRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException(
                    String.format("USer with id %d does not exists.", userId)
            );
        }

        return userDTOMapper.convertUserToUserDTO(user.get());
    }
}
