package com.aptech.proj4.repository;

import org.springframework.data.repository.CrudRepository;

import com.aptech.proj4.model.Assignee;


public interface AssigneeRepository extends CrudRepository<Assignee, Integer>  {
    
}