package com.aptech.proj4.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aptech.proj4.dto.ProjectDto;
import com.aptech.proj4.dto.TeamDto;
import com.aptech.proj4.model.Project;
import com.aptech.proj4.model.Team;
import com.aptech.proj4.repository.ProjectRepository;
import com.aptech.proj4.repository.TeamMemberRepository;
import com.aptech.proj4.repository.TeamRepository;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    ProjectRepository projectRepository;
    
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    TeamMemberRepository teamMemberRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProjectDto createProject(ProjectDto projectDto, String teamId) {
        Team team = teamRepository.findById(teamId)
            .orElseThrow(() -> new RuntimeException("Team ID not found"));

        Project project = new Project()
                .setId(Long.toString(System.currentTimeMillis()))
                .setName(projectDto.getName())
                .setTeam(team);
        
                
        projectRepository.save(project);
        return projectDto;
    }

    @Override
    public boolean deleteProject(String id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Project ID not found"));
        try {
            projectRepository.delete(project);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public List<Project> getAllProjects() {
        List<Project> projects = (List<Project>) projectRepository.findAll();
        return projects;
    }

    // @Override
    // public ProjectDto getProject(String id) {
    //     Optional<Project> project = Optional.ofNullable(projectRepository.findById(id).get());
    //     if (project.isPresent()) {
    //         return modelMapper.map(project.get(), ProjectDto.class);
    //     }
    //     throw new RuntimeException("Project does not exist");
    // }


    @Override
    public List<Project> findProjectByName(String name) {
        List<Project> projects = projectRepository.findByName(name);

        if (!projects.isEmpty()) {
            return projects;
        }
        throw new RuntimeException("Project name not found");
    }

}
