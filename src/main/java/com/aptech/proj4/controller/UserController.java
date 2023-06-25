package com.aptech.proj4.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aptech.proj4.dto.PasswordDto;
import com.aptech.proj4.dto.TokenRefreshDto;
import com.aptech.proj4.dto.UserDto;
import com.aptech.proj4.exception.TokenRefreshException;
import com.aptech.proj4.model.RefreshToken;
import com.aptech.proj4.model.User;
import com.aptech.proj4.model.UserRole;
import com.aptech.proj4.service.RefreshTokenService;
import com.aptech.proj4.service.UserService;
import com.aptech.proj4.utils.FileUploadUtil;
import com.aptech.proj4.utils.JwtUtils;

import jakarta.validation.Valid;

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
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart(value = "user") UserDto userDto) {
        try {
            UserDto user = new UserDto();
            if (image != null) {
                String fileName = StringUtils.cleanPath(image.getOriginalFilename());
                userDto.setPic(fileName);
                user = userService.signup(userDto);

                String uploadDir = "files/imgs/user-pp/";
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

    @PostMapping(path = "/create-admin", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @PreAuthorize("hasAnyAuthority('MAIN', 'ADMIN')")
    public ResponseEntity<?> createAdmin(
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart(value = "user") UserDto userDto) {
        try {
            UserDto admin = new UserDto();
            if (image != null) {
                String fileName = StringUtils.cleanPath(image.getOriginalFilename());
                userDto.setPic(fileName);
                admin = userService.createAdmin(userDto);

                String uploadDir = "files/imgs/user-pp/";
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
        List<User> users = userService.getAllUser();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/admin-role")
    @PreAuthorize("hasAnyAuthority('MAIN')")
    public ResponseEntity<?> changeAdminRole(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.changeAdminRole(userDto));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUser(@PathVariable String id, Authentication authentication) {
        try {
            String checkingEmail = authentication.getPrincipal().toString();
            UserDto checkingUser = userService.findUserByEmail(checkingEmail);
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            UserDto user = new UserDto();
            // check if user's own account or ADMIN or MAIN
            if (checkingUser.getId().equals(id)
                    || authorities.stream()
                            .anyMatch(role -> role.getAuthority().equals(UserRole.MAIN.toString())
                                    || role.getAuthority().equals(UserRole.ADMIN.toString()))) {
                user = userService.getUser(id);
                return ResponseEntity.ok(user);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("You do not have permission to access this page.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping(path = "/user/update", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> updateUser(
            Authentication authentication,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestPart(value = "user") UserDto userDto) {
        try {
            UserDto user = userService.findUserByEmail(authentication.getPrincipal().toString());
            user.setUsername(userDto.getUsername())
                    .setBio(userDto.getBio());

            UserDto result = new UserDto();
            if (image != null) {
                user.setPic(user.getId() + ".jpg");
                result = userService.updateUser(user);
                String uploadDir = "files/imgs/user-pp/";
                FileUploadUtil.saveFile(uploadDir, result.getPic(), image);
            } else {
                result = userService.updateUser(user);
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("user/change-password")
    public ResponseEntity<?> changePassword(@RequestBody PasswordDto passwordDto, Authentication authentication) {
        UserDto userDto = new UserDto().setEmail(authentication.getPrincipal().toString());
        try {
            return ResponseEntity.ok(userService.changePassword(userDto, passwordDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("user/delete/{id}")
    @PreAuthorize("hasAnyAuthority('MAIN', 'ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable String id, Authentication authentication) {
        try {
            return ResponseEntity.ok(userService.deleteUser(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshDto req) {
        String requestRefreshToken = req.getRefreshToken();
        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    return ResponseEntity.ok(jwtUtils.generateTokenFromEmail(user.getEmail()));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token is not in database!"));
    }

    @GetMapping(value = "/image/{pp}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> getImage(@PathVariable String pp) throws IOException {
        Path image = Paths.get("files/imgs/user-pp/" + pp);
        if (Files.exists(image)) {
            byte[] data = Files.readAllBytes(image);
            ByteArrayResource resource = new ByteArrayResource(data);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(resource);
        }
        return ResponseEntity.notFound().build();
    }
}
