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

    // Getter & Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNamaToko() {
        return namaToko;
    }

    public void setNamaToko(String namaToko) {
        this.namaToko = namaToko;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public User getKasir() {
        return kasir;
    }

    public void setKasir(User kasir) {
        this.kasir = kasir;
    }
}
