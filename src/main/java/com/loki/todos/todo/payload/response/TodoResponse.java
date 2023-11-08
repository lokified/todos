package com.loki.todos.todo.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class TodoResponse {
    private Long id;
    private String title;
    private String description;
    private String dueDate;
    private boolean isCompleted;
}
