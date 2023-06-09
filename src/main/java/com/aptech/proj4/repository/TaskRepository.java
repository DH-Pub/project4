package com.aptech.proj4.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.aptech.proj4.model.Project;
import com.aptech.proj4.model.Task;
import com.aptech.proj4.model.User;

public interface TaskRepository extends CrudRepository<Task, String>  {
    List<Task> findByProject(Project project);
    List<Task> findByUser(User user);
}
