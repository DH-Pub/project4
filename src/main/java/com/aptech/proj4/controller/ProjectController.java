package com.aptech.proj4.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aptech.proj4.dto.ProjectDto;
import com.aptech.proj4.model.Project;
import com.aptech.proj4.service.ProjectService;

@RestController
@RequestMapping("/projects")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @PostMapping("/create")
    public ResponseEntity<ProjectDto> createDocument(@RequestBody ProjectDto projectDto, Authentication authentication) {
        ProjectDto savedProject = projectService.createProject(projectDto, authentication.getPrincipal().toString());
        return ResponseEntity.ok(savedProject);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable("id") String id) {
         boolean deleted = projectService.deleteProject(id);
        if (deleted) {
            return ResponseEntity.ok("deleted");
        } else {
            return ResponseEntity.ok("failed");
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllProjects(Authentication authentication) {
        List<Project> projects = projectService.getAllProjects(authentication.getPrincipal().toString());
        if(projects != null) return ResponseEntity.ok(projects);
        else return null;
    }

    @GetMapping("/{name}")
    public ResponseEntity<Project> findProjectByName(@PathVariable("name")
    String name, Authentication authentication) {
    Project projectDto = projectService.findProjectByName(name, authentication.getPrincipal().toString());
    return ResponseEntity.ok(projectDto);
    }
}
