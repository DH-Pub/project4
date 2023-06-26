package com.aptech.proj4.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.aptech.proj4.dto.ProjectDto;
import com.aptech.proj4.model.Project;

@Service
public interface ProjectService {
    ProjectDto createProject(ProjectDto projectDto, String authentication);

    ProjectDto getProject(String id);

    boolean deleteProject(String id);

    List<Project> getAllProjects(String authentication);

    ProjectDto updateProject(ProjectDto projectDto);

     Project findProjectByName(String name, String authen); // use string `files`

}
