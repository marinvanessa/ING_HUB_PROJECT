package com.example.store_management_tool.service.exception;

public class AccessForbidenException extends RuntimeException{
    public AccessForbidenException(String message) {
        super(message);
    }
}
