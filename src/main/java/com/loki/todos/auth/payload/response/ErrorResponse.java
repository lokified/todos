package com.loki.todos.auth.payload.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ErrorResponse {
    private String message;
    private List<String> errors;

    public ErrorResponse(String message) {
        this.message = message;
        this.errors = new ArrayList<>();
    }

    public ErrorResponse(List<String> errors) {
        this.message = "invalid data";
        this.errors = errors;
    }
}
