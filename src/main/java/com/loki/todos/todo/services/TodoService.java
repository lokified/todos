package com.loki.todos.todo.services;

import com.loki.todos.auth.models.User;
import com.loki.todos.auth.repositories.UserRepository;
import com.loki.todos.auth.security.jwt.JwtUtils;
import com.loki.todos.todo.exceptions.TodoException;
import com.loki.todos.todo.exceptions.UnauthorizedAccessException;
import com.loki.todos.todo.models.Todo;
import com.loki.todos.todo.payload.request.TodoRequest;
import com.loki.todos.todo.payload.response.TodoResponse;
import com.loki.todos.todo.repositories.TodoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    public User validUser(String tokenHeader) {
        String token = tokenHeader.substring(7);
        if (!jwtUtils.validateJwtToken(token)) {
            throw new UnauthorizedAccessException();
        }
        String username = jwtUtils.getUsernameForJwt(token);
        return userRepository.findByUsername(username).orElseThrow(
                () -> new TodoException("User not found")
        );
    }

    public List<TodoResponse> getTodos(String tokenHeader) {
        User existingUser = validUser(tokenHeader);
        List<Todo> todos = todoRepository.findAllByUserId(existingUser.getId());

        return todos.stream().map( todo -> new TodoResponse(todo.getId(), todo.getTitle(), todo.getDescription(), todo.getDueDate(), todo.isCompleted()))
                .collect(Collectors.toList());
    }

    public void saveTodo(TodoRequest todo, String tokenHeader) {

        User existingUser = validUser(tokenHeader);

        Todo todo1 = new Todo();
        todo1.setTitle(todo.getTitle());
        todo1.setDescription(todo.getDescription());
        todo1.setDueDate(todo.getDueDate());
        todo1.setCompleted(todo.isCompleted());
        todo1.setUser(existingUser);

        todoRepository.save(todo1);

        existingUser.getTodos().add(todo1);
        userRepository.save(existingUser);
    }

    public String deleteTodo(Long id, String tokenHeader) {
        if (!jwtUtils.validateJwtToken(tokenHeader.substring(7))) {
            throw new UnauthorizedAccessException();
        }

        boolean exists = todoRepository.existsById(id);

        if (!exists) {
            throw new TodoException("Todo with id: " + id + " does not exits");
        }
        todoRepository.deleteById(id);
        return "deleted";
    }

    public List<TodoResponse> searchByTitle(String title, String tokenHeader) {

        if (title == null) {
            throw new TodoException("Title cannot be blank");
        }

        User user = validUser(tokenHeader);

        return todoRepository.findAllByTitle(title).stream()
                .map( todo -> new TodoResponse(todo.getId(), todo.getTitle(), todo.getDescription(), todo.getDueDate(), todo.isCompleted()))
                .collect(Collectors.toList());
    }

    @Transactional
    public String updateTodo(Todo todo, String tokenHeader) {

        if (!jwtUtils.validateJwtToken(tokenHeader.substring(7))) {
            throw new UnauthorizedAccessException();
        }

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