package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "transaksi")
public class Transaksi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime tanggal = LocalDateTime.now();

    private double totalBayar;

    private String pembayaran;

    @JsonManagedReference // Untuk menghindari infinite loop dengan ItemTransaksi
    @OneToMany(mappedBy = "transaksi", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemTransaksi> items;

    // Getter dan Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTanggal() {
        return tanggal;
    }

    public void setTanggal(LocalDateTime tanggal) {
        this.tanggal = tanggal;
    }

    public double getTotalBayar() {
        return totalBayar;
    }

    public void setTotalBayar(double totalBayar) {
        this.totalBayar = totalBayar;
    }

    public String getPembayaran() {
        return pembayaran;
    }

    public void setPembayaran(String pembayaran) {
        this.pembayaran = pembayaran;
    }

    public List<ItemTransaksi> getItems() {
        return items;
    }

    public void setItems(List<ItemTransaksi> items) {
        this.items = items;
        // Relasi dua arah: pastikan setiap item punya referensi balik ke transaksi
        if (items != null) {
            for (ItemTransaksi item : items) {
                item.setTransaksi(this);
            }
        }
    }
}
