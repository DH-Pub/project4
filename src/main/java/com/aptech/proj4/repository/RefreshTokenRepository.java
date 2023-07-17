package com.aptech.proj4.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.aptech.proj4.model.RefreshToken;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    @Transactional
    @Modifying
    @Query("DELETE from RefreshToken rt where rt.user.id=:userId")
    void clearUserRefreshToken(@Param("userId") String userId);
}
