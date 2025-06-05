package com.hostel_booking.userservice.service;

import com.hostel_booking.userservice.config.JwtUtil;
import com.hostel_booking.userservice.dto.LoginRequest;
import com.hostel_booking.userservice.dto.LoginResponse;
import com.hostel_booking.userservice.dto.SignupRequest;
import com.hostel_booking.userservice.model.User;
import com.hostel_booking.userservice.model.UserProfile;
import com.hostel_booking.userservice.repository.UserProfileRepository;
import com.hostel_booking.userservice.repository.UserRepository;

import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;
    public UserService(JwtUtil jwtUtil, UserRepository userRepository,
                       UserProfileRepository userProfileRepository,
                       PasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void signup(SignupRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user = userRepository.save(user);

        UserProfile profile = new UserProfile();
        profile.setFullName(request.getFullName());
        profile.setGender(request.getGender());
        profile.setPhoneNumber(request.getPhoneNumber());
        profile.setUser(user);
        userProfileRepository.save(profile);
    }

    public LoginResponse login(LoginRequest request, JwtUtil jwtUtil) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        return new LoginResponse(token,user.getEmail(), user.getRole());
    }

    public UserProfile getProfile(Long userId) {
        return userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
    }

    @Transactional
    public void updateProfile(Long userId, UserProfile updatedProfile) {
        UserProfile profile = getProfile(userId);
        profile.setFullName(updatedProfile.getFullName());
        profile.setGender(updatedProfile.getGender());
        profile.setPhoneNumber(updatedProfile.getPhoneNumber());
        userProfileRepository.save(profile);
    }
}
