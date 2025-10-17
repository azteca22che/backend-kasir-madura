package com.example.demo.controller;

import com.example.demo.dto.LoginRequest;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.util.Date;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;  // Inject PasswordEncoder

    private static final String SECRET = "mySuperSecretKey12345678901234567"; // minimal 32 byte

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        // Validasi input
        if (loginRequest.getUsername() == null || loginRequest.getPassword() == null ||
                loginRequest.getUsername().isEmpty() || loginRequest.getPassword().isEmpty()) {
            throw new UnauthorizedException("Username atau password tidak boleh kosong");
        }

        // Cari user berdasarkan username
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new UnauthorizedException("Username tidak ditemukan"));

        // Cek kecocokan password menggunakan passwordEncoder
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Password salah");
        }

        // Generate token
        long expirationTime = 1000 * 60 * 60 * 24; // 24 jam
        SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

        String jwtToken = Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Return token + role + message
        return new LoginResponse(jwtToken, user.getRole(), "Login berhasil");
    }

    // Error handling
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public ErrorResponse handleUnauthorized(UnauthorizedException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    // DTO untuk response sukses login
    public static class LoginResponse {
        private String token;
        private String role;
        private String message;

        public LoginResponse(String token, String role, String message) {
            this.token = token;
            this.role = role;
            this.message = message;
        }

        public String getToken() {
            return token;
        }

        public String getRole() {
            return role;
        }

        public String getMessage() {
            return message;
        }
    }

    // DTO untuk response error
    public static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    // Exception untuk login gagal
    public static class UnauthorizedException extends RuntimeException {
        public UnauthorizedException(String message) {
            super(message);
        }
    }
}
