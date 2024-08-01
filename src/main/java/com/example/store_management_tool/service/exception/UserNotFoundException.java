package com.example.store_management_tool.service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    @Getter
    public final String id;

    public UserNotFoundException(String id) {
        super();
        this.id = id;
    }
}