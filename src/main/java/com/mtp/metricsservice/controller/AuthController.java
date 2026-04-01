package com.mtp.metricsservice.controller;

import com.mtp.metricsservice.annotation.RateLimit;
import com.mtp.metricsservice.dto.AuthRequest;
import com.mtp.metricsservice.dto.JwtResponse;
import com.mtp.metricsservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth")
    @RateLimit("auth-endpoint")
    public ResponseEntity<JwtResponse> createAuthenticationToken(@Valid @RequestBody AuthRequest authRequest) {
        String jwtToken = authService.authenticateClient(authRequest.clientId());
        return ResponseEntity.ok(new JwtResponse(jwtToken));
    }

}





