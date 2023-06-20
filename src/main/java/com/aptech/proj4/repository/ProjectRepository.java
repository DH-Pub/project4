package com.aptech.proj4.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.aptech.proj4.model.Project;

public interface ProjectRepository  extends CrudRepository<Project, String>{
    Optional<Project> findByName(String name);
}
