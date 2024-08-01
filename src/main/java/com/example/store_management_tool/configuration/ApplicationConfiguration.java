package com.example.store_management_tool.configuration;

import com.example.store_management_tool.util.JWTUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public JWTUtil jwtUtil() {
        return new JWTUtil();
    }
}