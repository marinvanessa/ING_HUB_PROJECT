package com.example.store_management_tool.service;

import com.example.store_management_tool.controller.dto.UserLoginRequestDto;
import com.example.store_management_tool.controller.dto.UserRegistrationRequestDto;
import com.example.store_management_tool.coverter.UserConverter;
import com.example.store_management_tool.repository.UserRepository;
import com.example.store_management_tool.service.exception.UserWithThisEmailAlreadyExistsException;
import com.example.store_management_tool.service.model.User;
import com.example.store_management_tool.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final JWTUtil jwtUtil;
    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder;


    public void registerUser(UserRegistrationRequestDto userRegistrationDto) {
        if (repository.existsByEmail(userRegistrationDto.getEmail())) {
            throw new UserWithThisEmailAlreadyExistsException(userRegistrationDto.getEmail());
        }
        repository.save(userConverter.convertUserRegistrationRequestDtoToUser(userRegistrationDto));
    }

    public String login(UserLoginRequestDto loginRequestDto) {
        User user = repository.findByEmail(loginRequestDto.getEmail()).orElseThrow(() -> new
                UsernameNotFoundException("Username was not found"));

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");

        }
        return jwtUtil.generateToken(user.getEmail(), user.getRole().name());
    }

    public User getUserByEmail(String email) {
        return repository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Username was not found"));
    }

}
