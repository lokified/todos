package com.loki.todos.todo.models;

import com.loki.todos.auth.models.User;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "todo")
public class Todo {

    @Id
    @SequenceGenerator(
            name = "todo_sequence",
            sequenceName = "todo_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "todo_sequence"
    )
    private Long id;
    private String title;
    private String description;
    private String dueDate;
    private boolean isCompleted;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
}