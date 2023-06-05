package com.aptech.proj4.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.aptech.proj4.model.Team;


public interface TeamRepository extends CrudRepository<Team, Long> {
    Optional<Team> findById(String id);
}
