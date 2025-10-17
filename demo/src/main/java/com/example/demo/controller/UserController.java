package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Tambahkan encoder

    // Ambil semua user
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Ambil user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Tambah pengguna (otomatis jadi KASIR)
    @PostMapping
    public User createUser(@RequestBody User user) {
        user.setRole("KASIR");
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Hash password
        return userRepository.save(user);
    }

    // Sign up (otomatis jadi ADMIN)
    @PostMapping("/signup")
    public User signUp(@RequestBody User user) {
        user.setRole("ADMIN");
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Hash password
        return userRepository.save(user);
    }

    // Update pengguna
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        User user = optionalUser.get();

        if (userDetails.getUsername() != null) {
            user.setUsername(userDetails.getUsername());
        }
        if (userDetails.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword())); // Hash password baru
        }
        if (userDetails.getPhone() != null) {
            user.setPhone(userDetails.getPhone());
        }
        if (userDetails.getRole() != null) {
            user.setRole(userDetails.getRole());
        }
        if (userDetails.getNama() != null) {
            user.setNama(userDetails.getNama());
        }

        User updatedUser = userRepository.save(user);
        return ResponseEntity.ok(updatedUser);
    }

    // Hapus pengguna
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
