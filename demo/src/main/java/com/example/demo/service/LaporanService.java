package com.example.demo.service;

import com.example.demo.dto.LaporanResponse;
import com.example.demo.model.ItemTransaksi;
import com.example.demo.model.Transaksi;
import com.example.demo.repository.TransaksiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class LaporanService {

    @Autowired
    private TransaksiRepository transaksiRepository;

    public List<LaporanResponse> getLaporanByTokoAndTanggal(Long tokoId, LocalDate tanggal) {
        List<Transaksi> transaksiList = transaksiRepository.findByTokoIdAndTanggal(tokoId, tanggal);
        List<LaporanResponse> laporanResponses = new ArrayList<>();

        for (Transaksi transaksi : transaksiList) {
            double totalTransaksi = 0;
            List<LaporanResponse.ProdukItem> produkList = new ArrayList<>();

            // Ganti TransaksiDetail → ItemTransaksi
            for (ItemTransaksi item : transaksi.getItems()) {
                double totalProduk = item.getJumlah() * item.getHarga();
                totalTransaksi += totalProduk;

                produkList.add(new LaporanResponse.ProdukItem(
                        item.getProduk().getNama(),
                        item.getJumlah(),
                        item.getHarga(),
                        totalProduk
                ));
            }

            laporanResponses.add(new LaporanResponse(
                    transaksi.getToko().getNamaToko(),
                    transaksi.getKasir().getUsername(),
                    transaksi.getTanggal(),
                    produkList,
                    totalTransaksi
            ));
        }

        return laporanResponses;
    }
}
