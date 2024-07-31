package com.example.store_management_tool.controller.dto;

import com.example.store_management_tool.service.enums.Role;
import lombok.Getter;

@Getter
public class UserRegistrationRequestDto {

    private String email;
    private String password;
    private Role role;
}
