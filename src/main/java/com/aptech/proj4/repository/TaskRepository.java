package com.aptech.proj4.repository;

import org.springframework.data.repository.CrudRepository;

import com.aptech.proj4.model.Task;

public interface TaskRepository extends CrudRepository<Task, String>  {
    // List<Task> getAllTask();
}
