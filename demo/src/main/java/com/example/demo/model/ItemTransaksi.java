package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "item_transaksi")
public class ItemTransaksi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore // Hindari infinite loop saat serialisasi JSON (misalnya saat get transaksi)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaksi_id")
    private Transaksi transaksi;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "produk_id")
    private Produk produk;

    private int jumlah;

    private double harga;

    // Getter dan Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Transaksi getTransaksi() {
        return transaksi;
    }

    public void setTransaksi(Transaksi transaksi) {
        this.transaksi = transaksi;
    }

    public Produk getProduk() {
        return produk;
    }

    public void setProduk(Produk produk) {
        this.produk = produk;
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
