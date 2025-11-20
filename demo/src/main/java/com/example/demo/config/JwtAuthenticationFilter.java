package com.example.demo.config;

import com.example.demo.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 🔍 Ambil header Authorization
        String header = request.getHeader("Authorization");

        // 🔍 Validasi "Bearer <token>"
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            String token = header.substring(7); // ambil token setelah "Bearer "

            try {
                // 🔍 Validasi token dan ambil claim di dalamnya
                Claims claims = JwtUtil.validateToken(token);

                // 🔍 Ambil username dari token
                String username = claims.getSubject();

                // ⛔ MASALAH DI SINI DULU: kamu ambil "role", padahal token pakai "roles"
                //     .claim("roles", List.of(user.getRole()));  -> dari AuthController

                // 🔍 Ambil list roles dari token
                List<String> roles = claims.get("roles", List.class);

                String role = null;
                if (roles != null && !roles.isEmpty()) {
                    role = roles.get(0);
                }

                System.out.println("➡️ USER: " + username + " | ROLE: " + role);

                if (username != null && role != null) {

                    final String finalRole = role;  // ⬅️ FIX: jadikan final
                    GrantedAuthority authority = () -> finalRole;

                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    username,
                                    null,
                                    Collections.singleton(authority)
                            );

                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(auth);
                }


            } catch (Exception e) {
                // Jika token tidak valid
                System.err.println("❌ Invalid JWT: " + e.getMessage());
                SecurityContextHolder.clearContext();
            }
        }

        // Lanjutkan filter lain
        filterChain.doFilter(request, response);
    }
}
