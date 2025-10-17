package com.example.demo.controller;

import com.example.demo.dto.SignupRequest;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SignupController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;  // Inject PasswordEncoder

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public SignupResponse signup(@RequestBody SignupRequest signupRequest) {
        // Validasi input sederhana
        if (signupRequest.getUsername() == null || signupRequest.getPassword() == null || signupRequest.getPhone() == null
                || signupRequest.getUsername().isEmpty() || signupRequest.getPassword().isEmpty() || signupRequest.getPhone().isEmpty()) {
            throw new BadRequestException("Username, password, dan nomor handphone wajib diisi");
        }

        // Cek username sudah ada?
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new BadRequestException("Username sudah digunakan");
        }

        // Buat user baru dan simpan ke DB dengan password yang sudah di-hash
        User newUser = new User();
        newUser.setUsername(signupRequest.getUsername());
        newUser.setPassword(passwordEncoder.encode(signupRequest.getPassword())); // hash password
        newUser.setPhone(signupRequest.getPhone());
        userRepository.save(newUser);

        return new SignupResponse("Signup berhasil");
    }

    // Response class
    public static class SignupResponse {
        private String message;

        public SignupResponse(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }

    // Exception BadRequest
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ErrorResponse handleBadRequest(BadRequestException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    // ErrorResponse class
    public static class ErrorResponse {
        private String message;
        public ErrorResponse(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }

    // Exception class
    public static class BadRequestException extends RuntimeException {
        public BadRequestException(String message) {
            super(message);
        }
    }
}
