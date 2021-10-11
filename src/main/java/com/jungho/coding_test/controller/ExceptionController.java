package com.jungho.coding_test.controller;

import com.jungho.coding_test.dto.ErrorResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler({MethodArgumentNotValidException.class, IllegalArgumentException.class})
    public ErrorResult validException(
            MethodArgumentNotValidException e) {

        return new ErrorResult("BAD", e.getMessage());
    }
}
