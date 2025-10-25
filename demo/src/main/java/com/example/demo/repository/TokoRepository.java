package com.example.demo.repository;

import com.example.demo.model.Toko;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokoRepository extends JpaRepository<Toko, Long> {
    Toko findByKasir(User user);
}
