package com.aptech.proj4.utils;

import static com.aptech.proj4.config.SecurityConstants.SECRET;
import static com.aptech.proj4.config.SecurityConstants.TOKEN_EXPERITION_TIME;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.aptech.proj4.dto.AccessTokenDto;
import com.aptech.proj4.model.User;
import com.aptech.proj4.repository.UserRepository;
import com.aptech.proj4.service.RefreshTokenService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtils {
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private UserRepository userRepository;

    public AccessTokenDto generateToken(Authentication auth) {
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) auth
                .getPrincipal();
        String login = user.getUsername();

        Claims claims = Jwts.claims().setSubject(login);
        List<String> roles = new ArrayList<>();
        user.getAuthorities().stream().forEach(authority -> roles.add(authority.getAuthority()));
        claims.put("roles", roles);
        String token = Jwts.builder().setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPERITION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET).compact();

        AccessTokenDto accessToken = new AccessTokenDto();
        accessToken.setRoles(roles);
        accessToken.setToken(token);
        accessToken.setRefreshToken(refreshTokenService.createRefreshToken(login).getToken());
        return accessToken;
    }

    public AccessTokenDto generateTokenFromEmail(String email) {
        User user = userRepository.findByEmail(email).get();
        Claims claims = Jwts.claims().setSubject(email);
        List<String> roles = new ArrayList<>();
        // user.getRole().stream().forEach(authority ->
        // roles.add(authority.getRole().toString()));
        roles.add(user.getRole().toString());
        claims.put("roles", roles);
        String token = Jwts.builder().setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPERITION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET).compact();

        AccessTokenDto accessToken = new AccessTokenDto();
        accessToken.setRoles(roles);
        accessToken.setToken(token);
        accessToken.setRefreshToken(refreshTokenService.createRefreshToken(email).getToken());
        return accessToken;
    }
}
