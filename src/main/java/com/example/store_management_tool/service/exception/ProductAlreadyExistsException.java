package com.example.store_management_tool.service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ProductAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = -123456789L;

    @Getter
    public final String id;

    public ProductAlreadyExistsException(String id) {
        super();
        this.id = id;
    }

    public String getMessage() {
        return "Product with id " + id + " already exists";
    }
}