package com.loki.todos.controllers;

import com.loki.todos.models.Todo;
import com.loki.todos.services.TodoService;
import jakarta.persistence.QueryHint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("api/v1")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping("/todos")
    public List<Todo> getTodos() {
       return todoService.getTodos();
    }

    @GetMapping("/search")
    public List<Todo> search(@RequestParam("title") String title) {
        return todoService.searchByTitle(title);
    }

    @PostMapping("/addTodo")
    public void addTodo(@RequestBody Todo todo) {
        todoService.saveTodo(todo);
    }

    @PutMapping("/updateTodo")
    public String updateTodo(@RequestBody Todo todo) {
        return todoService.updateTodo(todo);
    }

    @DeleteMapping("/deleteTodo/{id}")
    public String deleteTodo(@PathVariable Long id) {
        return todoService.deleteTodo(id);
    }
}
