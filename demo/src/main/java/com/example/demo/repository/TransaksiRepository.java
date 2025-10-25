package com.example.demo.repository;

import com.example.demo.model.Transaksi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransaksiRepository extends JpaRepository<Transaksi, Long> {

    /**
     * Cari semua transaksi yang terjadi di antara waktu mulai dan waktu selesai.
     * Contohnya untuk laporan transaksi hari ini bisa pakai start = awal hari,
     * dan end = akhir hari.
     *
     * @param start waktu mulai
     * @param end waktu selesai
     * @return list transaksi di rentang waktu tersebut
     */
    List<Transaksi> findByTanggalBetween(LocalDateTime start, LocalDateTime end);

    List<Transaksi> findByTokoIdAndTanggal(Long tokoId, LocalDate tanggal);
}
