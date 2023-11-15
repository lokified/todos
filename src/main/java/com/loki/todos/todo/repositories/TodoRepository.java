package com.loki.todos.todo.repositories;

import com.loki.todos.todo.models.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    @Query("SELECT t FROM Todo t WHERE t.title LIKE %:title%")
    List<Todo> findAllByTitle(String title);

    List<Todo> findAllByUserId(Long userId);
}