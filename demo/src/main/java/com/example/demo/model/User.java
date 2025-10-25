package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
public class User {
    @OneToOne(mappedBy = "kasir")
    private Toko toko;

    // Getter dan Setter
    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Getter
    @Column(unique = true, nullable = false)
    private String username;

    @Setter
    @Getter
    @Column(nullable = false)
    private String password;

    @Setter
    @Getter
    private String phone;

    @Setter
    @Getter
    private String role;  // role bisa ADMIN, KASIR, dll

    @Setter
    @Getter
    private String nama;

}
