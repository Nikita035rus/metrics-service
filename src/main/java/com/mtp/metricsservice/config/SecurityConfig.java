package com.mtp.metricsservice.config;

import com.mtp.metricsservice.security.JwtRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Отключаем CSRF, так как используем JWT
                .csrf(AbstractHttpConfigurer::disable)

                // Настройка доступа к эндпоинтам
                .authorizeHttpRequests(auth -> auth
                        // Разрешаем всем доступ к авторизации (получение токена)
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        // Разрешаем доступ к консоли H2 для удобства проверки (если нужно)
                        .requestMatchers("/h2-console/**").permitAll()
                        // Все остальные запросы (включая /metrics) требуют валидного JWT
                        .anyRequest().authenticated()
                )

                // Отключаем сессии (каждый запрос должен нести токен)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Разрешаем отображение фреймов для консоли H2
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                );

        // Добавляем наш JWT фильтр перед стандартным фильтром аутентификации
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}


