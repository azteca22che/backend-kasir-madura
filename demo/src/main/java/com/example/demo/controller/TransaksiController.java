package com.example.demo.controller;

import com.example.demo.dto.ItemTransaksiDTO;
import com.example.demo.dto.TransaksiDTO;
import com.example.demo.model.ItemTransaksi;
import com.example.demo.model.Produk;
import com.example.demo.model.Transaksi;
import com.example.demo.repository.ProdukRepository;
import com.example.demo.repository.TransaksiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/transaksi")
public class TransaksiController {

    @Autowired
    private ProdukRepository produkRepository;

    @Autowired
    private TransaksiRepository transaksiRepository;

    @PostMapping
    public ResponseEntity<String> buatTransaksi(@RequestBody TransaksiDTO transaksiDTO) {
        try {
            if (transaksiDTO.getItems() == null || transaksiDTO.getItems().isEmpty()) {
                return ResponseEntity.badRequest().body("Daftar item transaksi tidak boleh kosong");
            }

            Transaksi transaksi = new Transaksi();
            transaksi.setPembayaran(transaksiDTO.getPembayaran());

            List<ItemTransaksi> itemEntities = new ArrayList<>();
            double totalBayar = 0;

            for (ItemTransaksiDTO itemDTO : transaksiDTO.getItems()) {
                if (itemDTO.getProdukId() == null) {
                    return ResponseEntity.badRequest().body("Produk ID pada item tidak boleh kosong");
                }
                if (itemDTO.getJumlah() <= 0) {
                    return ResponseEntity.badRequest().body("Jumlah pada item harus lebih dari 0");
                }
                if (itemDTO.getHarga() <= 0) {
                    return ResponseEntity.badRequest().body("Harga pada item harus lebih dari 0");
                }

                Produk produk = produkRepository.findById(itemDTO.getProdukId())
                        .orElseThrow(() -> new RuntimeException("Produk tidak ditemukan dengan id: " + itemDTO.getProdukId()));

                int stokBaru = produk.getStok() - itemDTO.getJumlah();
                if (stokBaru < 0) {
                    return ResponseEntity.badRequest().body("Stok tidak cukup untuk produk: " + produk.getNama());
                }

                produk.setStok(stokBaru);
                produkRepository.save(produk);

                ItemTransaksi item = new ItemTransaksi();
                item.setProduk(produk);
                item.setJumlah(itemDTO.getJumlah());
                item.setHarga(itemDTO.getHarga());
                item.setTransaksi(transaksi);

                itemEntities.add(item);

                totalBayar += itemDTO.getHarga() * itemDTO.getJumlah();
            }

            transaksi.setItems(itemEntities);
            transaksi.setTotalBayar(totalBayar);
            transaksi.setTanggal(LocalDateTime.now()); // set tanggal transaksi sekarang
            transaksiRepository.save(transaksi);

            return ResponseEntity.ok("Transaksi berhasil");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Gagal membuat transaksi: " + e.getMessage());
        }
    }

    // Ambil semua transaksi tanpa filter
    @GetMapping
    public ResponseEntity<List<TransaksiDTO>> getAllTransaksi() {
        try {
            List<Transaksi> transaksiList = transaksiRepository.findAll();

            List<TransaksiDTO> dtoList = new ArrayList<>();
            for (Transaksi t : transaksiList) {
                dtoList.add(convertToDTO(t));
            }

            return ResponseEntity.ok(dtoList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    // 🔥 Endpoint baru: Ambil transaksi hari ini
    @GetMapping("/today")
    public ResponseEntity<List<TransaksiDTO>> getTransaksiHariIni() {
        try {
            LocalDate today = LocalDate.now();
            LocalDateTime startOfDay = today.atStartOfDay();
            LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

            List<Transaksi> transaksiHariIni = transaksiRepository
                    .findByTanggalBetween(startOfDay, endOfDay);

            List<TransaksiDTO> dtoList = new ArrayList<>();
            for (Transaksi t : transaksiHariIni) {
                dtoList.add(convertToDTO(t));
            }

            return ResponseEntity.ok(dtoList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    // Helper method konversi entity Transaksi ke DTO
    private TransaksiDTO convertToDTO(Transaksi t) {
        TransaksiDTO dto = new TransaksiDTO();
        dto.setId(t.getId());
        dto.setPembayaran(t.getPembayaran());
        dto.setTotalBayar(t.getTotalBayar());
        dto.setTanggal(t.getTanggal().toString());

        List<ItemTransaksiDTO> itemDTOs = new ArrayList<>();
        for (ItemTransaksi item : t.getItems()) {
            ItemTransaksiDTO itemDTO = new ItemTransaksiDTO();
            itemDTO.setProdukId(item.getProduk().getId());
            itemDTO.setNamaProduk(item.getProduk().getNama());
            itemDTO.setHarga(item.getHarga());
            itemDTO.setJumlah(item.getJumlah());
            itemDTOs.add(itemDTO);
        }
        dto.setItems(itemDTOs);

        return dto;
    }
}
