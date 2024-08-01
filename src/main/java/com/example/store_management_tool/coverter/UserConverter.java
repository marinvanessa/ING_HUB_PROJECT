package com.example.store_management_tool.coverter;

import com.example.store_management_tool.controller.dto.UserRegistrationRequestDto;
import com.example.store_management_tool.service.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserConverter {
    private final PasswordEncoder passwordEncoder;


    public User convertUserRegistrationRequestDtoToUser(UserRegistrationRequestDto userRegistrationRequestDto) {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail(userRegistrationRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(userRegistrationRequestDto.getPassword()));
        user.setRole(userRegistrationRequestDto.getRole());
        return user;
    }
}
