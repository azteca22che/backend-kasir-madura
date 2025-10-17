package com.example.demo.controller;

import com.example.demo.dto.ProdukRequest;
import com.example.demo.model.Produk;
import com.example.demo.repository.ProdukRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produk")
public class ProdukController {

    @Autowired
    private ProdukRepository produkRepository;

    // Endpoint untuk menambahkan produk baru
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Produk addProduk(@RequestBody ProdukRequest request) {
        Produk produk = new Produk();
        produk.setNama(request.getNama());
        produk.setHargaJual(request.getHargaJual());
        produk.setStok(request.getStok());
        produk.setSatuan(request.getSatuan());

        return produkRepository.save(produk);
    }

    // Endpoint untuk mengambil semua produk
    @GetMapping
    public List<Produk> getAllProduk() {
        return produkRepository.findAll();
    }

    // Endpoint untuk mengubah produk berdasarkan ID
    @PutMapping("/update/{id}")
    public Produk updateProduk(@PathVariable Long id, @RequestBody ProdukRequest request) {
        Produk produk = produkRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produk tidak ditemukan dengan id: " + id));

        produk.setNama(request.getNama());
        produk.setHargaJual(request.getHargaJual());
        produk.setStok(request.getStok());
        produk.setSatuan(request.getSatuan());

        return produkRepository.save(produk);
    }
}
