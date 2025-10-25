package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // REST API tanpa session
                .authorizeHttpRequests(auth -> auth
                        // ✅ Endpoint publik (tanpa login)
                        .requestMatchers(
                                "/api/login",
                                "/api/users/signup",
                                "/api/auth/**",
                                "/api/hello"
                        ).permitAll()

                        // ✅ ADMIN-only endpoints
                        .requestMatchers("/api/toko/**").hasRole("ADMIN")
                        .requestMatchers("/api/user/**").hasRole("ADMIN")

                        // ✅ ADMIN & KASIR boleh akses
                        .requestMatchers("/api/produk/**", "/api/transaksi/**").hasAnyRole("ADMIN", "KASIR")

                        // Selain itu wajib login
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    // ✅ Password encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ✅ Hilangkan prefix "ROLE_" agar Spring Security cocok dengan role di DB (ADMIN / KASIR)
    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults(""); // hilangkan "ROLE_" prefix
    }

    // ✅ CORS supaya Flutter bisa akses API
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*");
            }
        };
    }
}
