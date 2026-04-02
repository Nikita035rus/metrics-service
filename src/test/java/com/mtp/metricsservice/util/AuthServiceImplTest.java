package com.mtp.metricsservice.util;

import com.mtp.metricsservice.exception.InvalidClientIdException;
import com.mtp.metricsservice.service.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void testAuthenticateClient_ValidClientId_ReturnsToken() {
        String clientId = "client-12345";
        String expectedToken = "mock-jwt-token";

        when(jwtUtil.generateToken(clientId)).thenReturn(expectedToken);

        String actualToken = authService.authenticateClient(clientId);

        assertThat(actualToken).isEqualTo(expectedToken);
    }


    @Test
    void testAuthenticateClient_EmptyClientId_ThrowsException() {
        assertThatThrownBy(() -> authService.authenticateClient(""))
                .isInstanceOf(InvalidClientIdException.class)
                .hasMessage("Client ID cannot be null or empty");
    }
}

