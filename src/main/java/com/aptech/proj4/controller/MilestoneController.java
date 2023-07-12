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

import com.aptech.proj4.dto.MilestoneDto;
import com.aptech.proj4.dto.ProjectDto;
import com.aptech.proj4.model.Milestone;
import com.aptech.proj4.service.MilestoneService;

@RestController
@RequestMapping("/milestone")
public class MilestoneController {
    @Autowired
    private MilestoneService milestoneService;

    @PostMapping("/create")
    public ResponseEntity<?> createProject(@RequestBody MilestoneDto milestoneDto,
            @RequestParam String projectId) {
        try {
            return ResponseEntity.ok(milestoneService.createMilestone(milestoneDto, projectId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        // ProjectDto savedProject = projectService.createProject(projectDto,
        // authentication.getPrincipal().toString());
        // return ResponseEntity.ok(savedProject);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMilestone(@PathVariable("id") String id) {
        boolean deleted = milestoneService.deleteMilestone(id);
        if (deleted) {
            return ResponseEntity.status(HttpStatus.OK).body("Milestone delete successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

      @GetMapping("/get/{id}")
    public ResponseEntity<?> getMilestone(@PathVariable("id") String id) {
        try {
            MilestoneDto milestone = milestoneService.getMilestone(id);
            return ResponseEntity.ok(milestone);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<MilestoneDto>> getAllMilestone() {
        List<Milestone> milestones = milestoneService.getAllMilestones();
        List<MilestoneDto> dtos = new ArrayList<>();
        for (Milestone mil : milestones) {
            MilestoneDto dto = new MilestoneDto();
            dto.setId(mil.getId()).setName(mil.getName()).setDescription(mil.getDescription()).setFrom(mil.getFrom())
                    .setTo(mil.getTo())
                    .setProjects_id(mil.getProject().getId());
            dtos.add(dto);
        }

        return ResponseEntity.ok(dtos);

    }

    @GetMapping("/{name}")
    public ResponseEntity<List<MilestoneDto>> findMilestoneByName(@PathVariable("name") String name) {
        List<Milestone> milestones = milestoneService.findMilestoneByName(name);

        List<MilestoneDto> dtos = new ArrayList<>();
        for (Milestone mil : milestones) {
            MilestoneDto dto = new MilestoneDto();
            dto.setId(mil.getId()).setName(mil.getName()).setDescription(mil.getDescription()).setFrom(mil.getFrom())
                    .setTo(mil.getTo())
                    .setProjects_id(mil.getProject().getId());
            dtos.add(dto);
        }

        return ResponseEntity.ok(dtos);
    }

@PutMapping("/{id}")
  public ResponseEntity<MilestoneDto> updateMilestone(@PathVariable String id, @RequestBody MilestoneDto milestoneDto) {
    MilestoneDto updatedMilestone = milestoneService.updateMilestone(id, milestoneDto);
    if (updatedMilestone != null) {
      return ResponseEntity.ok(updatedMilestone);
    }
    return ResponseEntity.notFound().build();
  }

}
