package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProdukRequest {
    private Long id;
    private String nama;
    private double hargaJual;
    private int stok;
    private String satuan;

    // Getters dan Setters

}
