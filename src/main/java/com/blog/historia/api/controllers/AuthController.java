package com.blog.historia.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.historia.api.payload.request.LoginRequest;
import com.blog.historia.api.payload.request.RegisterRequest;
import com.blog.historia.api.payload.response.AuthResponse;
import com.blog.historia.api.security.services.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

	private final AuthService authService;

	@PostMapping("/signin")
	public ResponseEntity<AuthResponse> authenticateUser(@Valid @RequestBody LoginRequest request) {
		return ResponseEntity.ok(authService.login(request));
	}

	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
		return ResponseEntity.ok(authService.register(request));
	}
	
	@GetMapping("/validate")
    public ResponseEntity<String> validateAuth() {
        return ResponseEntity.ok("Validado correctamente");
    }
}
