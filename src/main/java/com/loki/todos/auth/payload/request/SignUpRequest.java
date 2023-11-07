package com.loki.todos.auth.payload.request;

import com.loki.todos.auth.validation.PasswordMatching;
import com.loki.todos.auth.validation.StrongPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
@PasswordMatching(
        password = "password",
        confirmPassword = "confirmPassword",
        message = "Password and ConfirmPassword must be matched"
)
public class SignUpRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Email
    @Size(max = 50)
    private String email;

    private Set<String> role;

    @StrongPassword
    private String password;

    private String confirmPassword;
}
