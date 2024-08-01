package com.example.store_management_tool.advice;

import com.example.store_management_tool.service.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class AdviceController {
    private static final String ERROR_REQUEST = "Error occurred: ";

    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<String> handleProductAlreadyExistsException(ProductAlreadyExistsException ex) {
        log.debug(ERROR_REQUEST, ex);
        log.error("Product already exists: {}", ex.getMessage());
        String message = String.format("Product with id %s already exists", ex.getId());
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handleProductNotFoundException(ProductNotFoundException ex) {
        log.debug(ERROR_REQUEST, ex);
        log.error("Product not found: {}", ex.getMessage());
        String message = String.format("Product with id %s was not found", ex.getId());
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OrderItemNotFoundException.class)
    public ResponseEntity<String> handleOrderItemNotFoundException(OrderItemNotFoundException ex) {
        log.debug(ERROR_REQUEST, ex);
        log.error("Item not found: {}", ex.getMessage());
        String message = String.format("Item with id %s was not found", ex.getId());
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<String> handleOrderNotFoundException(OrderNotFoundException ex) {
        log.debug(ERROR_REQUEST, ex);
        log.error("Order not found: {}", ex.getMessage());
        String message = String.format("Order with id %s was not found", ex.getId());
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        log.debug(ERROR_REQUEST, ex);
        log.error("User not found: {}", ex.getMessage());
        String message = String.format("User with id %s was not found", ex.getId());
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserWithThisEmailAlreadyExistsException.class)
    public ResponseEntity<String> handleUserWithThisEmailAlreadyExistsException(UserWithThisEmailAlreadyExistsException ex) {
        log.debug(ERROR_REQUEST, ex);
        log.error("User already exists: {}", ex.getMessage());
        String message = String.format("User with email %s already exists", ex.getEmail());
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessForbiddenException.class)
    public ResponseEntity<String> handleAccessForbiddenException(AccessForbiddenException ex) {
        log.debug(ERROR_REQUEST, ex);
        log.error("Access forbidden: {}", ex.getMessage());
        String message = String.format("Access forbidden for user with id %s", ex.getId());
        return new ResponseEntity<>(message, HttpStatus.FORBIDDEN);
    }


    @ExceptionHandler(ItemNotFoundInsideOrderException.class)
    public ResponseEntity<String> handleItemNotFoundInsideOrderException(ItemNotFoundInsideOrderException ex) {
        log.debug(ERROR_REQUEST, ex);
        log.error("Item not found inside order: {}", ex.getMessage());
        String message = String.format("Item with id %s  inside order was not found", ex.getId());
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }



}
