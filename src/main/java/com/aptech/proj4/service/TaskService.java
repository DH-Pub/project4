package com.aptech.proj4.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.aptech.proj4.dto.TaskDto;
import com.aptech.proj4.model.Task;

public interface TaskService {
    boolean createTask(MultipartFile[] files, TaskDto task, String creatingUser);
    List<Task> getAllTasks();
    TaskDto getTaskById(String id, String authentication);
    List<Task> getTasksByProject(String projectId);
    TaskDto updateTask(TaskDto task);
}
