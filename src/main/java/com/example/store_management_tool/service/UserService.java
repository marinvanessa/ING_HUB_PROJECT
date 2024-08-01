package com.example.store_management_tool.service;

import com.example.store_management_tool.controller.dto.UserLoginRequestDto;
import com.example.store_management_tool.controller.dto.UserRegistrationRequestDto;
import com.example.store_management_tool.repository.UserRepository;
import com.example.store_management_tool.service.model.User;
import com.example.store_management_tool.util.JWTUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    @Transactional
    public void registerUser(UserRegistrationRequestDto userRegistrationDto) {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail(userRegistrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));
        user.setRole(userRegistrationDto.getRole());
        repository.save(user);
    }

    public String login(UserLoginRequestDto loginRequestDto) {
        User user = repository.findByEmail(loginRequestDto.getEmail()).orElseThrow(() -> new
                UsernameNotFoundException("User was not found"));

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");

        }
        return jwtUtil.generateToken(user.getEmail(), user.getRole().name());
    }

    public User getUserByEmail(String email) {
        return repository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

}
