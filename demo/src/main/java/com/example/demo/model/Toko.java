package com.example.demo.model;

import jakarta.persistence.*;
@Entity
public class Toko {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String namaToko;
    private String alamat;

    @OneToOne
    @JoinColumn(name = "kasir_id")
    private User kasir;

    public void getClass(Object namaToko) {
    }

    public void setAlamat(Object alamat) {
    }

    public void setKasir(User user) {

    }

    public String getNamaToko() {
        return "";
    }

    // Getter & Setter
}
