package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "item_transaksi")
public class ItemTransaksi {

    // Getter dan Setter
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

}
