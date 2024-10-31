package com.rockgustavo.desafiocrm.rest.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.rockgustavo.desafiocrm.exception.CustomerNotFoundException;
import com.rockgustavo.desafiocrm.exception.InsufficientStockException;
import com.rockgustavo.desafiocrm.exception.OrderNotFoundException;
import com.rockgustavo.desafiocrm.rest.ApiErrors;

@RestControllerAdvice
public class ApplicationControllerAdvice {

    // codigo 404
    @ExceptionHandler(CustomerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrors handleCustomerNotFound(CustomerNotFoundException ex) {
        return new ApiErrors(ex.getMessage());
    }

    // codigo 404
    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrors handleOrderNotFound(OrderNotFoundException ex) {
        return new ApiErrors(ex.getMessage());
    }

    // codigo 400
    @ExceptionHandler(InsufficientStockException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleInsufficientStock(InsufficientStockException ex) {
        return new ApiErrors(ex.getMessage());
    }

    // codigo 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());
        return new ApiErrors(errors);
    }

    // codigo 400
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleIllegalArgument(IllegalArgumentException ex) {
        return new ApiErrors(ex.getMessage());
    }

    // codigo 500
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiErrors handleGenericException(Exception ex) {
        ex.printStackTrace(); // Logando a exceção para análise
        return new ApiErrors("Ocorreu um erro interno. Por favor, tente novamente mais tarde.");
    }
}
