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

public class LoginResponse {
    public String token;
    public String email;
    public Role role;


}
