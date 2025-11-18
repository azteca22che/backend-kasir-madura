package com.example.demo.controller;

import com.example.demo.dto.TokoRequest;
import com.example.demo.model.Toko;
import com.example.demo.model.User;
import com.example.demo.repository.TokoRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/toko")
@RequiredArgsConstructor
public class TokoController {

    private final TokoRepository tokoRepository;
    private final UserRepository userRepository;

    @PostMapping("/add")
    public ResponseEntity<?> addToko(@RequestBody TokoRequest request) {
        // cari kasir berdasarkan ID
        Optional<User> kasirOpt = userRepository.findById(request.getKasirId());
        if (kasirOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Kasir tidak ditemukan");
        }

        // buat objek toko baru
        Toko toko = new Toko();
        toko.setNamaToko(request.getNamaToko());
        toko.setAlamat(request.getAlamat());
        toko.setKasir(kasirOpt.get());

        tokoRepository.save(toko);

        return ResponseEntity.ok(toko);
    }

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
