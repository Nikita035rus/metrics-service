package com.mtp.metricsservice.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = {
        RedisAutoConfiguration.class,
        RedisReactiveAutoConfiguration.class,
        RedisRepositoriesAutoConfiguration.class
})
class RateLimitIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RedisTemplate<String, Object> redisTemplate;

    @MockitoBean
    private RedisConnectionFactory redisConnectionFactory;

    @Test
    @WithMockUser(authorities = "CLIENT", username = "unique-user")
    void shouldReturn429WhenRateLimitExceeded() throws Exception {
        @SuppressWarnings("unchecked")
        ValueOperations<String, Object> valueOperations = (ValueOperations<String, Object>) mock(ValueOperations.class);

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        // Эмулируем поведение: 10 запросов проходят (возвращаем 1..10), на 11-й лимит (11)
        Long[] values = new Long[22];
        for (int i = 0; i < 22; i++) {
            values[i] = (long) (i / 2 + 1);
        }

        when(valueOperations.increment(anyString())).thenReturn(
                values[0], Arrays.copyOfRange(values, 1, values.length)
        );

        String metricJson = """
                {
                    "timestamp": "2023-10-10T10:00:00Z",
                    "value": 100.5,
                    "payload": {}
                }
                """;

        // Проверяем первые 10 запросов
        for (int i = 0; i < 10; i++) {
            mockMvc.perform(post("/api/v1/metrics")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(metricJson))
                    .andExpect(status().isAccepted());
        }

        // Проверяем 11-й запрос
        mockMvc.perform(post("/api/v1/metrics")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(metricJson))
                .andExpect(status().isTooManyRequests())
                .andExpect(jsonPath("$.error").value("rate_limit_exceeded"));
    }
}
