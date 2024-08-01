package com.example.store_management_tool.service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class OrderNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -123456789L;

    @Getter
    public final String id;

    public OrderNotFoundException(String id) {
        super();
        this.id = id;
    }

    public String getMessage() {
        return "Order with id " + id + " inside order was not found";
    }

}
