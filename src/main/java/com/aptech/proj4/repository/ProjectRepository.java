package com.aptech.proj4.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.aptech.proj4.model.Project;


public interface ProjectRepository  extends CrudRepository<Project, String>{
    List<Project> findByName(String name);
    Optional<Project> findById(String id);
}
