package com.example.store_management_tool.service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UserWithThisEmailAlreadyExistsException extends RuntimeException {

    @Getter
    public final String email;


    public UserWithThisEmailAlreadyExistsException(String email) {
        super("User with email " + email + " already exists");
        this.email = email;
    }
}
