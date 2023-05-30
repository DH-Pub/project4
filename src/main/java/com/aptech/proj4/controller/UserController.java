package com.aptech.proj4.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aptech.proj4.config.FileUploadUtil;
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

    @PostMapping(path = "/signup", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> signup(
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestPart(value = "user") UserDto userDto) {
        try {
            UserDto user = new UserDto();
            if (image != null) {
                String fileName = StringUtils.cleanPath(image.getOriginalFilename());
                userDto.setPic(fileName);
                user = userService.signup(userDto);

                String uploadDir = "imgs/user-pp/";
                FileUploadUtil.saveFile(uploadDir, user.getPic(), image);
            } else {
                userDto.setPic(null);
                user = userService.signup(userDto);
            }
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not upload picture.");
        }
    }

    // @PostMapping("/create-admin")
    // public ResponseEntity<UserDto> createAdmin(@RequestBody UserDto userDto,
    // Authentication authentication) {
    // Collection<? extends GrantedAuthority> authorities =
    // authentication.getAuthorities();
    // if (authorities.stream().anyMatch(role ->
    // role.getAuthority().equals(UserRole.MAIN.toString()))) {
    // return ResponseEntity.ok(userService.createAdmin(userDto));
    // }
    // return ResponseEntity.badRequest().build();
    // }

    @PostMapping(path = "/create-admin", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @PreAuthorize("hasAnyAuthority('MAIN', 'ADMIN')")
    public ResponseEntity<?> createAdmin(
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestPart(value = "user") UserDto userDto) {
        try {
            UserDto admin = new UserDto();
            if (image != null) {
                String fileName = StringUtils.cleanPath(image.getOriginalFilename());
                userDto.setPic(fileName);
                admin = userService.createAdmin(userDto);

                String uploadDir = "imgs/user-pp/";
                FileUploadUtil.saveFile(uploadDir, admin.getPic(), image);
            } else {
                userDto.setPic(null);
                admin = userService.createAdmin(userDto);
            }
            return ResponseEntity.ok(admin);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not upload picture.");
        }
    }

    @GetMapping("/all-users")
    @PreAuthorize("hasAnyAuthority('MAIN', 'ADMIN')")
    public ResponseEntity<?> getAllUser() {
        return ResponseEntity.ok(userService.getAllUser());
    }

    @PostMapping("/admin-role")
    @PreAuthorize("hasAnyAuthority('MAIN')")
    public ResponseEntity<?> changeAdminRole(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.changeAdminRole(userDto));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUser(@PathVariable String id) {
        try {
            return ResponseEntity.ok(userService.getUser(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User does not exist.");
        }
    }
}
