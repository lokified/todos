package com.loki.todos.todo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException() {
        super("You Are Unauthorized to perform this action");
    }

    public UnauthorizedAccessException(String message) {
        super(message);
    }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ResponseBody
    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<?> userIdNotValidExceptionHandler(UnauthorizedAccessException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("status", "Failure");
        response.put("message", ex.getMessage());
        response.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response.toString());
    }
}
