package com.aptech.proj4.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aptech.proj4.dto.UserDto;
import com.aptech.proj4.service.RefreshTokenService;
import com.aptech.proj4.service.UserService;
import com.aptech.proj4.utils.JwtUtils;

@RestController
@RequestMapping("/auth")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    RefreshTokenService refreshTokenService;
    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserDto userDto) {
        try {
            return ResponseEntity.ok(userService.signup(userDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists.");
        }
    }

    // @PostMapping("/create-admin")
    // public ResponseEntity<UserDto> createAdmin(@RequestBody UserDto userDto,
    //         Authentication authentication) {
    //     Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    //     if (authorities.stream().anyMatch(role -> role.getAuthority().equals(UserRole.MAIN.toString()))) {
    //         return ResponseEntity.ok(userService.createAdmin(userDto));
    //     }
    //     return ResponseEntity.badRequest().build();
    // }

    @PostMapping("/create-admin")
    @PreAuthorize("hasAnyAuthority('MAIN', 'ADMIN')")
    public ResponseEntity<UserDto> createAdmin(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.createAdmin(userDto));
    }
}
