package com.example.survey_uni.utils;

import com.example.survey_uni.service.CustomUserDetailsService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Xác minh jwt token cho mỗi request
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        System.out.println("FILTER START");
        final String authHeader = request.getHeader("Authorization");
        System.out.println("AUTH HEADER = " + authHeader);

        String email = null;
        String jwt = null;

        // Xét token
        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            try {
                email = jwtUtil.extractUsername(jwt);
            } catch (JwtException e) {
                logger.warn(e.getMessage());
            }
        }

        System.out.println("JWT = " + jwt);

        // Xét user đã đăng nhập với SecurityContextHolder
        if(email != null
                && SecurityContextHolder
                        .getContext()
                        .getAuthentication() == null){
            UserDetails userDetails = userDetailsService.loadUserByEmail(email);
            System.out.println(userDetails.getAuthorities());

            // Xét token hợp lệ thì login với credential(password) là null
            if(jwtUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // Gắn token vào SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
            System.out.println("SET AUTH SUCCESS: " + email);
        }
        System.out.println("EMAIL = " + email);
        // Cho request đi tiếp
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().equals("/api/auth/login");
    }
}
