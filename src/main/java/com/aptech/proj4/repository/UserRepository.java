package com.aptech.proj4.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.aptech.proj4.model.User;


public interface UserRepository extends CrudRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findById(String id);
}
