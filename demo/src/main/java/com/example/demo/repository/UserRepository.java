package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);

    // Digunakan saat login untuk mencari user berdasarkan username
    Optional<User> findByUsername(String username);
    //untuk forgot password
    Optional<User> findByPhone(String phone);

}
