package com.example.demo.config; // ganti sesuai dengan package kamu

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // Mengizinkan semua endpoint tanpa autentikasi (untuk sekarang)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Nonaktifkan CSRF untuk REST API
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // Ijinkan semua request
                );
        return http.build();
    }

    // Bean untuk hashing password dengan BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
