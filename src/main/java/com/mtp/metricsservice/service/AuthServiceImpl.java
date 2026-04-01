package com.mtp.metricsservice.service;

import com.mtp.metricsservice.exception.InvalidClientIdException;
import com.mtp.metricsservice.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtUtil jwtUtil;

    @Override
    public String authenticateClient(String clientId) {
        validateClientId(clientId);
        return jwtUtil.generateToken(clientId);
    }

    private void validateClientId(String clientId) {
        if (clientId == null || clientId.trim().isEmpty()) {
            throw new InvalidClientIdException("Client ID cannot be null or empty");
        }
        if (!isValidClientIdFormat(clientId)) {
            throw new InvalidClientIdException(
                    "Client ID must start with 'client-' and contain only digits after the dash");
        }
    }

    private boolean isValidClientIdFormat(String clientId) {
        return clientId.startsWith("client-")
                && clientId.length() >= 8
                && clientId.matches("client-\\d+");
    }
}


