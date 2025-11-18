package com.example.demo.service;

import com.example.demo.model.OtpVerification;
import com.example.demo.repository.OtpVerificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OtpService {

    private static final Logger log = LoggerFactory.getLogger(OtpService.class);
    private final OtpVerificationRepository otpRepo;

    public OtpService(OtpVerificationRepository otpRepo) {
        this.otpRepo = otpRepo;
    }

    /**
     * Generate OTP acak 4 digit, simpan ke database, dan tampilkan di log (console)
     */
    @Transactional
    public String generateAndSaveOtp(String phone) {
        // 🔹 OTP acak 4 digit
        String otp = String.format("%04d", new Random().nextInt(9000) + 1000);
        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(5);

        otpRepo.deleteByPhone(phone); // hapus OTP lama jika ada
        otpRepo.save(new OtpVerification(phone, otp, expiredAt));

        // ✅ Gunakan logger agar muncul di console
        log.info("✅ OTP untuk {}: {}", phone, otp);

        return otp;
    }

    /**
     * Verifikasi apakah OTP cocok dan belum kedaluwarsa
     */
    public boolean verifyOtp(String phone, String otp) {
        return otpRepo.findByPhone(phone)
                .filter(o -> o.getOtp().equals(otp) && o.getExpiredAt().isAfter(LocalDateTime.now()))
                .isPresent();
    }

    /**
     * Hapus OTP setelah diverifikasi atau kadaluarsa
     */
    @Transactional
    public void deleteOtp(String phone) {
        otpRepo.deleteByPhone(phone);
    }
}
