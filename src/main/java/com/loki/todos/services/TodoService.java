package com.loki.todos.services;

import com.loki.todos.models.Todo;
import com.loki.todos.repositories.TodoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    public List<Todo> getTodos() {
        return todoRepository.findAll();
    }

    public void saveTodo(Todo todo) {
        todoRepository.save(todo);
    }

    public String deleteTodo(Long id) {

        boolean exists = todoRepository.existsById(id);

        if (!exists) {
            throw new IllegalArgumentException("Todo with id: " + id + "does not exits");
        }
        todoRepository.deleteById(id);
        return "deleted";
    }

    public List<Todo> searchByTitle(String title) {
        if (title == null) {
            throw new IllegalArgumentException("Title cannot be blank");
        }
        return todoRepository.findAllByTitle(title);
    }

    @Transactional
    public String updateTodo(Todo todo) {

        Todo exisitingTodo = todoRepository.findById(todo.getId()).orElseThrow(
                () -> new IllegalArgumentException("Todo with id " + todo.getId() + " does not exist")
        );

        if (todo.getTitle() != null && !todo.getTitle().isEmpty() && !Objects.equals(exisitingTodo.getTitle(), todo.getTitle())) {
            exisitingTodo.setTitle(todo.getTitle());
        }

        if (todo.getDescription() != null && !todo.getDescription().isEmpty() && !Objects.equals(exisitingTodo.getDescription(), todo.getDescription())) {
            exisitingTodo.setDescription(todo.getDescription());
        }

        if (todo.getDueDate() != null && !todo.getDueDate().isEmpty() && !Objects.equals(exisitingTodo.getDueDate(), todo.getDueDate())) {
            exisitingTodo.setDueDate(todo.getDueDate());
        }

        if (!Objects.equals(exisitingTodo.isCompleted(), todo.isCompleted())) {
            exisitingTodo.setCompleted(todo.isCompleted());
        }

        return "updated";
    }
}