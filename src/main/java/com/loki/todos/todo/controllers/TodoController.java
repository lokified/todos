package com.loki.todos.todo.controllers;

import com.loki.todos.auth.payload.response.MessageResponse;
import com.loki.todos.auth.security.jwt.JwtUtils;
import com.loki.todos.todo.exceptions.TodoException;
import com.loki.todos.todo.exceptions.UnauthorizedAccessException;
import com.loki.todos.todo.models.Todo;
import com.loki.todos.todo.payload.request.TodoRequest;
import com.loki.todos.todo.payload.response.TodoResponse;
import com.loki.todos.todo.services.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Todo", description = "Apis for todo")
@RestController
@RequestMapping("api/todo")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @Operation(
            summary = "Gets all todos",
            description = "Returns a list of todo object."
    )
    @GetMapping
    public List<TodoResponse> getTodos(
            @RequestHeader(name = "Authorization")
            @Parameter(hidden = true) final String token
    ) {
        return todoService.getTodos(token);
    }

    @Operation(
            summary = "Searches for a todo by title",
            description = "Requires a query title and returns a list of todos."
    )
    @GetMapping("/search")
    public List<TodoResponse> search(
            @RequestParam("title") String title,
            @RequestHeader(name = "Authorization")
            @Parameter(hidden = true) final String token
    ) {
        return todoService.searchByTitle(title, token);
    }

    @Operation(
            summary = "Adds a new todo",
            description = "Returns a string."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = { @Content(schema = @Schema(implementation = MessageResponse.class), mediaType = "application/json") }
            )
    })
    @PostMapping("/addTodo")
    public ResponseEntity<?> addTodo(
            @Valid @RequestBody TodoRequest todo,
            @RequestHeader(name = "Authorization")
            @Parameter(hidden = true) final String token
    ) {
        todoService.saveTodo(todo, token);
        return ResponseEntity.ok(new MessageResponse("Todo added successfully"));
    }

    @Operation(
            summary = "Updates todo",
            description = "Make changes to a todo. Returns a string"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = { @Content(schema = @Schema(implementation = MessageResponse.class), mediaType = "application/json") }
            )
    })
    @PutMapping("/updateTodo")
    public ResponseEntity<?> updateTodo(
            @Valid @RequestBody Todo todo,
            @RequestHeader(name = "Authorization")
            @Parameter(hidden = true) final String token
    ) {
        String message = todoService.updateTodo(todo, token);
        return ResponseEntity.ok(new MessageResponse(message));
    }

    @Operation(
            summary = "Deletes a todo",
            description = "requires a todo id as a parameter. Returns a string."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = { @Content(schema = @Schema(implementation = MessageResponse.class), mediaType = "application/json") }
            )
    })
    @DeleteMapping("/deleteTodo/{id}")
    public ResponseEntity<?> deleteTodo(
            @PathVariable Long id,
            @RequestHeader(name = "Authorization")
            @Parameter(hidden = true) final String token
    ) {
        String message = todoService.deleteTodo(id, token);
        return ResponseEntity.ok(new MessageResponse(message));
    }
}
