package com.example.store_management_tool.service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ItemNotFoundInsideOrderException extends RuntimeException {

    private static final long serialVersionUID = -123456789L;

    @Getter
    private final String id;

    public ItemNotFoundInsideOrderException(String id) {
        super();
        this.id = id;
    }

    public String getMessage() {
        return "Item with id " + id + " inside order was not found";
    }
}