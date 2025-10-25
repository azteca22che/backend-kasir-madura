package com.example.demo.controller;

import com.example.demo.dto.LaporanResponse;
import com.example.demo.service.LaporanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/laporan")
public class LaporanController {

    @Autowired
    private LaporanService laporanService;

    // GET /api/laporan?tokoId=1&tanggal=2025-10-20
    @GetMapping
    public ResponseEntity<List<LaporanResponse>> getLaporan(
            @RequestParam Long tokoId,
            @RequestParam String tanggal) {

        LocalDate tanggalFilter = LocalDate.parse(tanggal);
        List<LaporanResponse> laporanList = laporanService.getLaporanByTokoAndTanggal(tokoId, tanggalFilter);

        return ResponseEntity.ok(laporanList);
    }
}
