package com.Tracy.tesiHotelBookingManagementSystem.controller;

import com.Tracy.tesiHotelBookingManagementSystem.dto.UserDTO;
import com.Tracy.tesiHotelBookingManagementSystem.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO userDTO) {
        userService.register(userDTO);
        return ResponseEntity.ok("User registered successfully");
    }
}