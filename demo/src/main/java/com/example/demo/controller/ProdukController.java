package com.example.demo.controller;

import com.example.demo.model.Produk;
import com.example.demo.repository.ProdukRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.util.List;

@RestController
@RequestMapping("/api/produk")
@CrossOrigin(origins = "*")
public class ProdukController {

    @Autowired
    private ProdukRepository produkRepository;

    private final String uploadDir = "uploads/";

    // ------------------------------
    // ADD PRODUK (multipart)
    // ------------------------------
    @PostMapping(value = "/add", consumes = "multipart/form-data")
    public Produk addProduk(
            @RequestParam("image") MultipartFile image,
            @RequestParam("nama") String nama,
            @RequestParam("hargaJual") double hargaJual,
            @RequestParam("stok") int stok,
            @RequestParam("satuan") String satuan
    ) throws IOException {

        // Buat folder jika belum ada
        Files.createDirectories(Paths.get(uploadDir));

        // Generate nama unik untuk gambar
        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();

        // Simpan file fisik
        Path filePath = Paths.get(uploadDir + fileName);
        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Simpan ke database
        Produk produk = new Produk();
        produk.setNama(nama);
        produk.setHargaJual(hargaJual);
        produk.setStok(stok);
        produk.setSatuan(satuan);
        produk.setImageName(fileName);

        return produkRepository.save(produk);
    }

    // ------------------------------
    // GET semua produk
    // ------------------------------
    @GetMapping
    public List<Produk> getAllProduk() {
        return produkRepository.findAll();
    }

    // ------------------------------
    // UPDATE PRODUK (gambar optional)
    // ------------------------------
    @PostMapping(value = "/update/{id}", consumes = "multipart/form-data")
    public Produk updateProduk(
            @PathVariable Long id,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam("nama") String nama,
            @RequestParam("hargaJual") double hargaJual,
            @RequestParam("stok") int stok,
            @RequestParam("satuan") String satuan
    ) throws IOException {

        Produk produk = produkRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produk tidak ditemukan dengan id: " + id));

        produk.setNama(nama);
        produk.setHargaJual(hargaJual);
        produk.setStok(stok);
        produk.setSatuan(satuan);

        // Jika gambar baru diupload → simpan
        if (image != null && !image.isEmpty()) {
            Files.createDirectories(Paths.get(uploadDir));

            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + fileName);

            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            produk.setImageName(fileName);
        }

        return produkRepository.save(produk);
    }

    // ------------------------------
    // GET GAMBAR (untuk Flutter)
    // ------------------------------
    @GetMapping("/gambar/{fileName}")
    public ResponseEntity<byte[]> getImage(@PathVariable String fileName) throws IOException {
        File img = new File(uploadDir + fileName);

        if (!img.exists()) {
            return ResponseEntity.notFound().build();
        }

        byte[] imageBytes = Files.readAllBytes(img.toPath());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE)
                .body(imageBytes);
    }

    // ------------------------------
// DELETE produk
// ------------------------------
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduk(@PathVariable Long id) {

        if (!produkRepository.existsById(id)) {
            return ResponseEntity.status(404).body("Produk tidak ditemukan");
        }

        produkRepository.deleteById(id);
        return ResponseEntity.ok("Produk berhasil dihapus");
    }

}
