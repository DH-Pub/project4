package com.aptech.proj4.service;

import java.util.Optional;

import com.aptech.proj4.model.RefreshToken;

public interface RefreshTokenService {
    public Optional<RefreshToken> findByToken(String token);
    public RefreshToken createRefreshToken(String email);
    public RefreshToken verifyExpiration(RefreshToken token);
}
