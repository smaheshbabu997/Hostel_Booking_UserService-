package com.hostel_booking.userservice.controller;

import com.hostel_booking.userservice.config.JwtUtil;
import com.hostel_booking.userservice.dto.LoginRequest;
import com.hostel_booking.userservice.dto.LoginResponse;
import com.hostel_booking.userservice.dto.SignupRequest;
import com.hostel_booking.userservice.model.UserProfile;
import com.hostel_booking.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired private JwtUtil jwtUtil;

    //user signup url
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequest request) {
        userService.signup(request);
        return ResponseEntity.ok("User registered");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request, jwtUtil));
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<UserProfile> getProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getProfile(userId));
    }

    @PutMapping("/profile/{userId}")
    public ResponseEntity<String> updateProfile(@PathVariable Long userId, @RequestBody UserProfile profile) {
        userService.updateProfile(userId, profile);
        return ResponseEntity.ok("Profile updated");
    }
}

