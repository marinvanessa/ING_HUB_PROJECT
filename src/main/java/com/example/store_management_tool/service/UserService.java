package com.example.store_management_tool.service;

import com.example.store_management_tool.controller.dto.UserRegistrationDto;
import com.example.store_management_tool.repository.UserRepository;
import com.example.store_management_tool.service.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

     public void registerUser(UserRegistrationDto userRegistrationDto) {
         User user = new User();
         user.setId(UUID.randomUUID());
         user.setEmail(userRegistrationDto.getEmail());
         user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));
         user.setRole(userRegistrationDto.getRole());
         repository.save(user);
     }

}
