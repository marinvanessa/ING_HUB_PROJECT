package com.example.store_management_tool.controller;

import com.example.store_management_tool.controller.dto.UserLoginRequestDto;
import com.example.store_management_tool.controller.dto.UserRegistrationRequestDto;
import com.example.store_management_tool.service.UserService;
import com.example.store_management_tool.service.exception.UserWithThisEmailAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationRequestDto registrationDto)
            throws UserWithThisEmailAlreadyExistsException {
        log.info("User with email {} will be registered", registrationDto.getEmail());
        userService.registerUser(registrationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    @PostMapping("/login")
    public String login(@RequestBody UserLoginRequestDto loginRequestDto) {
        return userService.login(loginRequestDto);
    }
}
