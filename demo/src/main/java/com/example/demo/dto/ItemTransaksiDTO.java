package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ItemTransaksiDTO {
    // Getter dan Setter
    private Long produkId;
    private String namaProduk; // digunakan hanya untuk output laporan
    private int jumlah;
    private double harga;

}
