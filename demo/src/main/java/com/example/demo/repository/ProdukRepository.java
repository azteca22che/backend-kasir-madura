package com.example.demo.repository;

import com.example.demo.model.Produk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdukRepository extends JpaRepository<Produk, Long> {
}
