package com.billboarding.Controller.AUTH;

import com.billboarding.DTO.AuthResponse;
import com.billboarding.DTO.LoginRequest;
import com.billboarding.DTO.RegisterRequest;
import com.billboarding.Entity.User;
import com.billboarding.Services.Auth.AuthService;
import com.billboarding.Services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody @Valid RegisterRequest request) {
        User saved = userService.register(request);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}

