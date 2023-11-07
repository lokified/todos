package com.loki.todos.auth.controllers;


import com.loki.todos.auth.models.User;
import com.loki.todos.auth.payload.request.LoginRequest;
import com.loki.todos.auth.payload.request.SignUpRequest;
import com.loki.todos.auth.payload.response.ErrorResponse;
import com.loki.todos.auth.payload.response.JwtResponse;
import com.loki.todos.auth.payload.response.MessageResponse;
import com.loki.todos.auth.repositories.UserRepository;
import com.loki.todos.auth.security.jwt.JwtUtils;
import com.loki.todos.auth.security.services.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication", description = "Apis for signup and signin")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;


    @Operation(
            summary = "Logs in a user",
            description = "A user should first have an account created. The response is an object of the user."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = { @Content(schema = @Schema(implementation = JwtResponse.class), mediaType = "application/json") }
            ),
            @ApiResponse(
                    responseCode = "400",
                    content = { @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json") }
            )
    })
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        if (!userRepository.existsByUsername(loginRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Username does not exist!"));
        }

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail()));
    }

    @Operation(
            summary = "Creates an account for new user",
            description = "User should have unique username and email. A user can be a normal user or a moderator or an admin or both user and moderator.  The response is a string."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = { @Content(schema = @Schema(implementation = MessageResponse.class), mediaType = "application/json") }
            ),
            @ApiResponse(
                    responseCode = "400",
                    content = { @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json") }
            )
    })
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new ErrorResponse(" Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Email is already in use!"));
        }

        User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(), passwordEncoder.encode(signUpRequest.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successful"));
    }
}
