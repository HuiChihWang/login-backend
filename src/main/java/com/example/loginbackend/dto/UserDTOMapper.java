package com.example.loginbackend.dto;

import com.example.loginbackend.entity.AppUser;
import org.springframework.stereotype.Component;


@Component
public class UserDTOMapper {
    public UserDTO convertUserToUserDTO(AppUser user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getEmail()
        );
    }
}
