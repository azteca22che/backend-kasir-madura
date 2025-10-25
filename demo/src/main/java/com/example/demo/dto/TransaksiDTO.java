package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class TransaksiDTO {

    // Getter dan Setter
    private Long id; // id transaksi
    private String pembayaran; // Metode pembayaran: QRIS, Tunai, dll
    private double totalBayar; // Total harga transaksi
    private String tanggal; // Tanggal transaksi, disimpan dalam format String (misal ISO8601)
    private List<ItemTransaksiDTO> items; // Daftar item dalam transaksi

}
