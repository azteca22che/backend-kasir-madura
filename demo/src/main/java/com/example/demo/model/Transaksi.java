package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@Table(name = "transaksi")
public class Transaksi {

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private LocalDateTime tanggal = LocalDateTime.now();

    @Setter
    private double totalBayar;

    @Setter
    private String pembayaran;

    // 🔹 Relasi ke Toko
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "toko_id")
    private Toko toko;

    // 🔹 Relasi ke User (kasir)
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kasir_id")
    private User kasir;

    // 🔹 Relasi ke ItemTransaksi
    @JsonManagedReference
    @OneToMany(mappedBy = "transaksi", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemTransaksi> items;

    // =================== GETTER SETTER ===================

    public void setItems(List<ItemTransaksi> items) {
        this.items = items;
        if (items != null) {
            for (ItemTransaksi item : items) {
                item.setTransaksi(this);
            }
        }
    }

}
