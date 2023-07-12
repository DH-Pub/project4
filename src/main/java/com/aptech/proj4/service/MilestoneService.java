package com.aptech.proj4.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.aptech.proj4.dto.MilestoneDto;
import com.aptech.proj4.dto.ProjectDto;
import com.aptech.proj4.model.Milestone;

@Service
public interface MilestoneService {
    MilestoneDto createMilestone(MilestoneDto milestoneDto, String projectId);

    boolean deleteMilestone(String id);

    List<Milestone> getAllMilestones();

    List<Milestone> findMilestoneByName(String name);

    MilestoneDto updateMilestone(String id, MilestoneDto milestone);

    MilestoneDto getMilestone(String id);

}
