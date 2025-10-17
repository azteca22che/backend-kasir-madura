package com.example.demo.dto;

public class ItemTransaksiDTO {
    private Long produkId;
    private String namaProduk; // digunakan hanya untuk output laporan
    private int jumlah;
    private double harga;

    // Getter dan Setter
    public Long getProdukId() {
        return produkId;
    }

    public void setProdukId(Long produkId) {
        this.produkId = produkId;
    }

    public String getNamaProduk() {
        return namaProduk;
    }

    public void setNamaProduk(String namaProduk) {
        this.namaProduk = namaProduk;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }
}
