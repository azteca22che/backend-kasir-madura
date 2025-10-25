package com.example.demo.controller;

import com.example.demo.dto.LoginRequest;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.OtpService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OtpService otpService;

    private static final String SECRET = "mySuperSecretKey12345678901234567"; // minimal 32 byte

    // ======================= LOGIN ==========================
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        if (loginRequest.getUsername() == null || loginRequest.getPassword() == null ||
                loginRequest.getUsername().isEmpty() || loginRequest.getPassword().isEmpty()) {
            throw new UnauthorizedException("Username atau password tidak boleh kosong");
        }

        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new UnauthorizedException("Username tidak ditemukan"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Password salah");
        }

        long expirationTime = 1000 * 60 * 60 * 24; // 24 jam
        SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

        String jwtToken = Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return new LoginResponse(jwtToken, user.getRole(), "Login berhasil");
    }

    // ==================== FORGOT PASSWORD ====================

    // 1️⃣ Kirim OTP ke nomor HP
    @PostMapping("/auth/send-otp")
    public ResponseEntity<ApiResponse> sendOtp(@RequestBody Map<String, String> request) {
        String phone = request.get("phone");

        if (phone == null || phone.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse("Nomor HP wajib diisi", null));
        }

        boolean exists = userRepository.findByPhone(phone).isPresent();
        if (!exists) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Nomor HP tidak terdaftar", null));
        }

        String otp = otpService.generateAndSaveOtp(phone);

        // ⚠️ Di production, jangan tampilkan OTP di log
        System.out.println("OTP dikirim ke " + phone + ": " + otp);

        return ResponseEntity.ok(new ApiResponse("Kode OTP telah dikirim ke nomor " + phone, null));
    }

    // 2️⃣ Verifikasi OTP
    @PostMapping("/auth/verify-otp")
    public ResponseEntity<ApiResponse> verifyOtp(@RequestBody Map<String, String> request) {
        String phone = request.get("phone");
        String otp = request.get("otp");

        if (phone == null || phone.isEmpty() || otp == null || otp.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse("Nomor HP dan OTP wajib diisi", null));
        }

        if (otpService.verifyOtp(phone, otp)) {
            otpService.deleteOtp(phone); // hapus OTP setelah berhasil diverifikasi
            return ResponseEntity.ok(new ApiResponse("OTP valid", null));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("OTP salah atau sudah kadaluarsa", null));
        }
    }

    // 3️⃣ Reset password
    @PostMapping("/auth/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(@RequestBody Map<String, String> request) {
        String phone = request.get("phone");
        String newPassword = request.get("newPassword");

        if (phone == null || phone.isEmpty() || newPassword == null || newPassword.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse("Nomor HP dan password baru wajib diisi", null));
        }

        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("Nomor HP tidak ditemukan"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        otpService.deleteOtp(phone);

        return ResponseEntity.ok(new ApiResponse("Password berhasil direset", null));
    }

    // =================== ERROR HANDLING =====================
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public ErrorResponse handleUnauthorized(UnauthorizedException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    // =================== DTO =====================
    @Getter
    @AllArgsConstructor
    public static class LoginResponse {
        private final String token;
        private final String role;
        private final String message;
    }

    @Getter
    @AllArgsConstructor
    public static class ErrorResponse {
        private final String message;
    }

    @Getter
    @AllArgsConstructor
    public static class ApiResponse {
        private final String message;
        private final Object data;
    }

    public static class UnauthorizedException extends RuntimeException {
        public UnauthorizedException(String message) {
            super(message);
        }
    }
}
