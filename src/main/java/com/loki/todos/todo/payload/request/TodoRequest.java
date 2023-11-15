package com.loki.todos.todo.payload.request;

import lombok.Data;

@Data
public class TodoRequest {

    private String title;
    private String description;
    private String dueDate;
    private boolean isCompleted;
}
