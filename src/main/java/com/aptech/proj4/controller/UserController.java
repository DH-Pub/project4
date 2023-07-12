package com.aptech.proj4.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
import com.aptech.proj4.enums.UserRole;
import com.aptech.proj4.exception.TokenRefreshException;
import com.aptech.proj4.model.RefreshToken;
import com.aptech.proj4.model.User;
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
            return ResponseEntity.badRequest().body("Email already exists.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Could not upload picture.");
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
            return ResponseEntity.badRequest().body("Email already exists.");
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Could not upload picture.");
        }
    }

    @GetMapping("/all-users")
    @PreAuthorize("hasAnyAuthority('MAIN', 'ADMIN')")
    public ResponseEntity<?> getAllUser() {
        try {
            List<User> users = userService.getAllUser();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/admin-role")
    @PreAuthorize("hasAnyAuthority('MAIN','ADMIN')")
    public ResponseEntity<?> changeAdminRole(@RequestBody UserDto userDto, Authentication authentication) {
        try {
            UserDto changingUser = userService.findUserByEmail(authentication.getPrincipal().toString());
            UserDto user = userService.findUserByEmail(userDto.getEmail());
            if (changingUser.getRole().equals(UserRole.ADMIN.toString())
                    && user.getRole().equals(UserRole.MAIN.toString())) {
                return ResponseEntity.badRequest().body("You don't have authority over this account");
            }

            // change Role auth
            boolean isAdmin = changingUser.getRole().equals(UserRole.ADMIN.toString());
            boolean adminAuth = userDto.getRole().equals(UserRole.MAIN.toString());
            if (isAdmin && adminAuth) {
                return ResponseEntity.badRequest().body("You don't have authority to change role to MAIN");
            }
            return ResponseEntity.ok(userService.changeAdminRole(userDto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/account")
    public ResponseEntity<?> getPersonalAccount(Authentication authentication) {
        try {
            UserDto user = userService.findUserByEmail(authentication.getPrincipal().toString());
            user.setPassword(null);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUser(@PathVariable String id, Authentication authentication) {
        try {
            UserDto checkingUser = userService.findUserByEmail(authentication.getPrincipal().toString());

            // check if user's own account or ADMIN or MAIN
            String matches = UserRole.MAIN.toString() + "|" + UserRole.ADMIN.toString();
            boolean allowed = authentication.getAuthorities().stream()
                    .anyMatch(role -> role.getAuthority().matches(matches));
            if (checkingUser.getId().equals(id) || allowed) {
                UserDto user = userService.getUser(id);
                user.setPassword(null);
                return ResponseEntity.ok(user);
            }
            return ResponseEntity.badRequest()
                    .body("You do not have permission to access this page.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping(path = "/user/update", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> updateUser(
            Authentication authentication,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestPart(value = "user", required = false) UserDto userDto) {
        try {
            UserDto user = userService.findUserByEmail(authentication.getPrincipal().toString());
            UserDto result = user;
            if (userDto != null) {
                user.setUsername(userDto.getUsername())
                        .setBio(userDto.getBio());
            }

            if (image != null) {
                user.setPic(user.getId() + ".jpg");
                result = userService.updateUser(user);
                String uploadDir = "files/imgs/user-pp/";
                FileUploadUtil.saveFile(uploadDir, result.getPic(), image);
            } else {
                result = userService.updateUser(user);
            }
            result.setPassword(null);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("user/change-password")
    public ResponseEntity<?> changePassword(@RequestBody PasswordDto passwordDto, Authentication authentication) {
        UserDto userDto = new UserDto().setEmail(authentication.getPrincipal().toString());
        try {
            return ResponseEntity.ok(userService.changePassword(userDto, passwordDto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("user/delete/{id}")
    @PreAuthorize("hasAnyAuthority('MAIN', 'ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable String id, Authentication authentication) {
        try {
            UserDto deletingUser = userService.findUserByEmail(authentication.getPrincipal().toString());
            UserDto user = userService.getUser(id);
            if (deletingUser.getRole().equals(UserRole.ADMIN.toString())
                    && user.getRole().equals(UserRole.MAIN.toString())) {
                return ResponseEntity.badRequest().body("You don't have authority over this account");
            }
            deleteImage(user.getId(), authentication);
            return ResponseEntity.ok(userService.deleteUser(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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

    @DeleteMapping("/image/delete/{id}")
    public ResponseEntity<?> deleteImage(@PathVariable String id, Authentication authentication) throws IOException {
        // Path image = Paths.get("files/imgs/user-pp/" + pp);
        try {
            UserDto user = userService.getUser(id);

            // check for admin permissions
            String matches = UserRole.MAIN.toString() + "|" + UserRole.ADMIN.toString();
            boolean allowed = authentication.getAuthorities().stream()
                    .anyMatch(role -> role.getAuthority().matches(matches));
            if (authentication.getPrincipal().toString().equals(user.getEmail()) ||
                    allowed) {
                Path image = Paths.get("files/imgs/user-pp/" + user.getPic());
                Files.delete(image);
                user.setPic(null);
                userService.updateUser(user);
                return ResponseEntity.ok(true);
            }

            return ResponseEntity.badRequest().body(false);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshDto req) {
        String requestRefreshToken = req.getRefreshToken();
        try {
            return refreshTokenService.findByToken(requestRefreshToken)
                    .map(refreshTokenService::verifyExpiration)
                    .map(RefreshToken::getUser)
                    .map(user -> {
                        return ResponseEntity.ok(jwtUtils.generateTokenFromEmail(user.getEmail()));
                    })
                    .orElseThrow(
                            () -> new TokenRefreshException(requestRefreshToken, "Refresh token is not in database!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
