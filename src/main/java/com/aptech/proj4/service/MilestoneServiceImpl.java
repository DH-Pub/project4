package com.aptech.proj4.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aptech.proj4.dto.MilestoneDto;
import com.aptech.proj4.model.Milestone;
import com.aptech.proj4.model.Project;
import com.aptech.proj4.repository.MilestoneRepository;
import com.aptech.proj4.repository.ProjectRepository;

@Service
public class MilestoneServiceImpl implements MilestoneService {

    @Autowired
    MilestoneRepository milestoneRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public MilestoneDto createMilestone(MilestoneDto milestoneDto, String projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project ID not found"));

        // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // LocalDate from = LocalDate.parse(milestoneDto.getFrom(), formatter);
        // LocalDate to = LocalDate.parse(milestoneDto.getTo(), formatter);

        Milestone milestone = new Milestone()
                .setId(Long.toString(System.currentTimeMillis()))
                .setName(milestoneDto.getName())
                .setDescription(milestoneDto.getDescription())
                .setFrom(milestoneDto.getFrom())
                .setTo(milestoneDto.getTo())
                .setProject(project);

        milestoneRepository.save(milestone);

        return milestoneDto;
    }

    @Override
    public boolean deleteMilestone(String id) {
        Milestone milestone = milestoneRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Milestone ID not found"));
        try {
            milestoneRepository.delete(milestone);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public List<Milestone> getAllMilestones() {
        List<Milestone> milestones = (List<Milestone>) milestoneRepository.findAll();
        return milestones;
    }

    @Override
    public List<Milestone> findMilestoneByName(String name) {
        List<Milestone> milestones = milestoneRepository.findByName(name);

        if (!milestones.isEmpty()) {
            return milestones;
        }
        throw new RuntimeException("Milestone name not found");
    }

    @Override
    public MilestoneDto updateMilestone(String id, MilestoneDto milestoneDto) {
        Milestone existingMilestone = milestoneRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Milestone ID not found"));

                existingMilestone.setName(milestoneDto.getName());
                existingMilestone.setDescription(milestoneDto.getDescription());
                existingMilestone.setFrom(milestoneDto.getFrom());
                existingMilestone.setTo(milestoneDto.getTo());

        Milestone updatedMilestone = milestoneRepository.save(existingMilestone);

        MilestoneDto updatedMilestoneDto = modelMapper.map(updatedMilestone, MilestoneDto.class);
        return updatedMilestoneDto;
    }

}
