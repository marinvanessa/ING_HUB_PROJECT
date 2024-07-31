package com.example.store_management_tool.controller;

import com.example.store_management_tool.controller.dto.UserLoginRequestDto;
import com.example.store_management_tool.controller.dto.UserRegistrationRequestDto;
import com.example.store_management_tool.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public void registerUser(@RequestBody UserRegistrationRequestDto registrationDto) {
        log.info("User with email {} will be registered", registrationDto.getEmail());
        userService.registerUser(registrationDto);
    }

    @PostMapping("/login")
    public String login(@RequestBody UserLoginRequestDto loginRequestDto) {
        userService.login(loginRequestDto);
        return "Login Successful!";
    }
}
