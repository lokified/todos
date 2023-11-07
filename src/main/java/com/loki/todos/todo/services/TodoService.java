package com.loki.todos.todo.services;

import com.loki.todos.auth.security.jwt.JwtUtils;
import com.loki.todos.todo.exceptions.TodoException;
import com.loki.todos.todo.models.Todo;
import com.loki.todos.todo.repositories.TodoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private JwtUtils jwtUtils;

    public boolean validator(String headerAuth) {

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            String cleanedToken = headerAuth.substring(7, headerAuth.length());

            return jwtUtils.validateJwtToken(cleanedToken);
        }

        return false;
    }

    public List<Todo> getTodos() {
        return todoRepository.findAll();
    }

    public void saveTodo(Todo todo) {
        todoRepository.save(todo);
    }

    public String deleteTodo(Long id) {

        boolean exists = todoRepository.existsById(id);

        if (!exists) {
            throw new TodoException("Todo with id: " + id + " does not exits");
        }
        todoRepository.deleteById(id);
        return "deleted";
    }

    public List<Todo> searchByTitle(String title) {
        if (title == null) {
            throw new TodoException("Title cannot be blank");
        }
        return todoRepository.findAllByTitle(title);
    }

    @Transactional
    public String updateTodo(Todo todo) {

        Todo exisitingTodo = todoRepository.findById(todo.getId()).orElseThrow(
                () -> new TodoException("Todo with id " + todo.getId() + " does not exist")
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