package com.aptech.proj4.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.aptech.proj4.model.RefreshToken;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long>{
    Optional<RefreshToken> findByToken(String token);
}
