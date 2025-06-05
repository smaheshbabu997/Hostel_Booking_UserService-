package com.hostel_booking.userservice.config;

import com.hostel_booking.userservice.model.User;
import com.hostel_booking.userservice.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        String email = null;
        String jwt = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            try {
                email = jwtUtil.extractUsername(jwt);
            } catch (Exception e) {
                logger.error("JWT parsing failed", e);
            }
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            Optional<User> userDetailsOpt = userRepository.findByEmail(email);
            if (userDetailsOpt.isPresent()) {
                UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                        userDetailsOpt.get().getEmail(),
                        userDetailsOpt.get().getPassword(),
                        List.of(new SimpleGrantedAuthority("ROLE_" + userDetailsOpt.get().getRole().name()))
                );

                if (jwtUtil.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}

