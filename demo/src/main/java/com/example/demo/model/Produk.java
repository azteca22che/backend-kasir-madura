package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "produk")
public class Produk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Penting supaya entitasnya ada primary key!

    private String nama;
    private int stok;
    private String satuan;

    @Column(name = "harga_jual")
    private double hargaJual; // ubah nama field dan mapping ke kolom harga_jual

    // Getter dan Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public double getHargaJual() {
        return hargaJual;
    }

    public void setHargaJual(double hargaJual) {
        this.hargaJual = hargaJual;
    }
}
