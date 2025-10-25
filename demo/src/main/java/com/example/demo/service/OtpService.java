package com.example.demo.service;

import com.example.demo.model.OtpVerification;
import com.example.demo.repository.OtpVerificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // ← Tambahkan ini

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OtpService {

    private final OtpVerificationRepository otpRepo;

    public OtpService(OtpVerificationRepository otpRepo) {
        this.otpRepo = otpRepo;
    }

    /**
     * Generate OTP acak 6 digit, simpan ke database, dan tampilkan di log (console)
     */
    @Transactional // ← Penting: aktifkan transaksi di sini
    public String generateAndSaveOtp(String phone) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(5);

        otpRepo.deleteByPhone(phone); // perlu transaksi aktif
        otpRepo.save(new OtpVerification(phone, otp, expiredAt));

        System.out.println("✅ OTP untuk " + phone + ": " + otp);
        return otp;
    }

    public boolean verifyOtp(String phone, String otp) {
        return otpRepo.findByPhone(phone)
                .filter(o -> o.getOtp().equals(otp) && o.getExpiredAt().isAfter(LocalDateTime.now()))
                .isPresent();
    }

    @Transactional // ← optional, tapi disarankan juga
    public void deleteOtp(String phone) {
        otpRepo.deleteByPhone(phone);
    }

    public String generateOtp(String phone) {
        return phone;
    }

    public void clearOtp(String phone) {
    }
}
