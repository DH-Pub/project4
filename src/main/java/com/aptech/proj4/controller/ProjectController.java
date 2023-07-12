package com.aptech.proj4.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<?> createProject(@RequestBody ProjectDto projectDto,
            @RequestParam String teamId) {
        try {
            return ResponseEntity.ok(projectService.createProject(projectDto, teamId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        // ProjectDto savedProject = projectService.createProject(projectDto,
        // authentication.getPrincipal().toString());
        // return ResponseEntity.ok(savedProject);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable("id") String id) {
        boolean deleted = projectService.deleteProject(id);
        if (deleted) {
            return ResponseEntity.status(HttpStatus.OK).body("Project delete successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

  @GetMapping("/get/{id}")
    public ResponseEntity<?> getProject(@PathVariable("id") String id) {
        try {
            ProjectDto project = projectService.getProject(id);
            return ResponseEntity.ok(project);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<ProjectDto>> getAllProjects() {
        List<Project> projects = projectService.getAllProjects();
        List<ProjectDto> dtos = new ArrayList<>();
        for (Project pro : projects) {
            ProjectDto dto = new ProjectDto();
            dto.setId(pro.getId()).setName(pro.getName()).setCreateAt(pro.getCreatedAt())
                    .setTeam_id(pro.getTeam().getId());
            dtos.add(dto);
        }

        return ResponseEntity.ok(dtos);

    }

    @GetMapping("/{name}")
    public ResponseEntity<List<ProjectDto>> findProjectByName(@PathVariable("name") String name) {
        List<Project> projects = projectService.findProjectByName(name);

        List<ProjectDto> dtos = new ArrayList<>();
        for (Project pro : projects) {
            ProjectDto dto = new ProjectDto();
            dto.setId(pro.getId()).setName(pro.getName()).setCreateAt(pro.getCreatedAt())
                    .setTeam_id(pro.getTeam().getId());
            dtos.add(dto);
        }

        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDto> updateProject(@PathVariable String id, @RequestBody ProjectDto projectDto) {
        ProjectDto updatedProject = projectService.updateproject(id, projectDto);
        if (updatedProject != null) {
            return ResponseEntity.ok(updatedProject);
        }
        return ResponseEntity.notFound().build();
    }

}
