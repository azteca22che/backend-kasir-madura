package com.example.demo.controller;

import com.example.demo.dto.TokoRequest;
import com.example.demo.model.Toko;
import com.example.demo.repository.TokoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.List;


@RestController
@RequestMapping("/api/toko")
@RequiredArgsConstructor
public class TokoController {
    private final TokoRepository tokoRepository;
    private final UserRepository userRepository;

    @GetMapping("/kasir/{kasirId}")
    public ResponseEntity<?> getTokoByKasir(@PathVariable Long kasirId) {
        Optional<User> kasir = userRepository.findById(kasirId);
        if (kasir.isEmpty()) {
            return ResponseEntity.badRequest().body("Kasir tidak ditemukan");
        }

        Toko toko = tokoRepository.findByKasir(kasir.get());
        if (toko == null) {
            return ResponseEntity.badRequest().body("Toko belum ditetapkan untuk kasir ini");
        }

        return ResponseEntity.ok(toko);
    }
}