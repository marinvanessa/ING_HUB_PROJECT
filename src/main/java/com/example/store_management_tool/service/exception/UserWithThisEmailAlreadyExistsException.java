package com.example.store_management_tool.service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UserWithThisEmailAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = -123456789L;

    @Getter
    public final String email;


    public UserWithThisEmailAlreadyExistsException(String email) {
        super();
        this.email = email;
    }

    public String getMessage() {
        return "User with email " + email + " already exists";
    }
}
