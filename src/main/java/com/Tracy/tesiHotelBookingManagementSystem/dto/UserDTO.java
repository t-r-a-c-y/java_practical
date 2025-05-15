package com.Tracy.tesiHotelBookingManagementSystem.dto;

import com.Tracy.tesiHotelBookingManagementSystem.entity.Role;
import lombok.Data;

@Data
public class UserDTO {
    private String name;
    private String email;
    private String password;
    private Role role;
}
