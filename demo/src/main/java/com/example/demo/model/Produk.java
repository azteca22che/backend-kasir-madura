package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "produk")
public class Produk {

    // Getter dan Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Penting supaya entitasnya ada primary key!

    private String nama;
    private int stok;
    private String satuan;

    @Column(name = "harga_jual")
    private double hargaJual; // ubah nama field dan mapping ke kolom harga_jual

}
