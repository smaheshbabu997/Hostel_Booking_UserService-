package com.hostel_booking.userservice.dto;

import com.hostel_booking.userservice.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class SignupRequest {
    private String email;
    private String password;
    private String fullName;
    private String gender;
    private String phoneNumber;
    private Role role;
}
