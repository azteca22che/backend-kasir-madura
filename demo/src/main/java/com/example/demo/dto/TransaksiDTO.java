package com.example.demo.dto;

import java.util.List;

public class TransaksiDTO {

    private Long id; // id transaksi
    private String pembayaran; // Metode pembayaran: QRIS, Tunai, dll
    private double totalBayar; // Total harga transaksi
    private String tanggal; // Tanggal transaksi, disimpan dalam format String (misal ISO8601)
    private List<ItemTransaksiDTO> items; // Daftar item dalam transaksi

    // Getter dan Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPembayaran() {
        return pembayaran;
    }

    public void setPembayaran(String pembayaran) {
        this.pembayaran = pembayaran;
    }

    public double getTotalBayar() {
        return totalBayar;
    }

    public void setTotalBayar(double totalBayar) {
        this.totalBayar = totalBayar;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public List<ItemTransaksiDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemTransaksiDTO> items) {
        this.items = items;
    }
}
