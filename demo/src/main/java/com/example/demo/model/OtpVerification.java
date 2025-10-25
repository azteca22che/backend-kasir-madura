package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "otp_verification")
public class OtpVerification {

    // Getters & Setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String phone;
    @Setter
    private String otp;
    @Setter
    private LocalDateTime expiredAt;

    public OtpVerification() {}

    public OtpVerification(String phone, String otp, LocalDateTime expiredAt) {
        this.phone = phone;
        this.otp = otp;
        this.expiredAt = expiredAt;
    }

}
