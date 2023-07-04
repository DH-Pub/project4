package com.aptech.proj4.service;

import java.util.List;

import com.aptech.proj4.dto.ProjectDto;
import com.aptech.proj4.dto.TaskDto;
import com.aptech.proj4.model.Project;


public interface ProjectService {
    ProjectDto createProject(ProjectDto projectDto, String teamId);

    boolean deleteProject(String id);

    List<Project> getAllProjects();

    List<Project> findProjectByName(String name); // use string `files`

    ProjectDto updateproject(String id, ProjectDto project);

    ProjectDto getProject(String id);

}
