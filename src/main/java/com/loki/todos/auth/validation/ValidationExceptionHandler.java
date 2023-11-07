package com.loki.todos.auth.validation;

import com.loki.todos.auth.payload.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> notValid(MethodArgumentNotValidException exception, HttpServletRequest request) {
        List<String> errors = new ArrayList<>();

        exception.getAllErrors().forEach( err -> errors.add(err.getDefaultMessage()));

        return ResponseEntity.badRequest().body (new ErrorResponse(errors));
    }
}
