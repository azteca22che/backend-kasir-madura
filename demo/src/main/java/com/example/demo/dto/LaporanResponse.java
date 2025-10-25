package com.example.demo.dto;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class LaporanResponse {
    private String namaToko;
    private String namaKasir;
    private LocalDate tanggal;
    private List<ProdukItem> produkTerjual;
    private double totalPenjualan;

    public LaporanResponse(String namaToko, String namaKasir, LocalDate tanggal,
                           List<ProdukItem> produkTerjual, double totalPenjualan) {
        this.namaToko = namaToko;
        this.namaKasir = namaKasir;
        this.tanggal = tanggal;
        this.produkTerjual = produkTerjual;
        this.totalPenjualan = totalPenjualan;
    }

    public LaporanResponse(Object namaToko, String username, LocalDateTime tanggal, List<ProdukItem> produkList, double totalTransaksi) {
    }

    @Getter
    public static class ProdukItem {
        private String namaProduk;
        private int jumlah;
        private double hargaSatuan;
        private double total;

        public ProdukItem(String namaProduk, int jumlah, double hargaSatuan, double total) {
            this.namaProduk = namaProduk;
            this.jumlah = jumlah;
            this.hargaSatuan = hargaSatuan;
            this.total = total;
        }

    }
}
