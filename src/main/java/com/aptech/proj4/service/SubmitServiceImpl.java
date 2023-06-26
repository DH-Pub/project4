package com.aptech.proj4.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.stereotype.Service;

import com.aptech.proj4.dto.SubmitDto;
import com.aptech.proj4.model.Submit;
import com.aptech.proj4.model.Task;
import com.aptech.proj4.repository.SubmitRepository;
import com.aptech.proj4.repository.TaskRepository;

@Service
public class SubmitServiceImpl implements SubmitService {
  @Autowired
  SubmitRepository submitRepository;

  @Autowired
  TaskRepository taskRepository;

  @Override
  public SubmitDto uploadSubmit(SubmitDto submitDto, String taskId) {
    Task task = taskRepository.findById(taskId)
        .orElseThrow(() -> new RuntimeException("Task not found"));

    Submit newSubmit = new Submit()
        .setId(Long.toString(System.currentTimeMillis()))
        .setNote(null);
    String attached = submitDto.getAttached() == null ? null : submitDto.getAttached();
    newSubmit.setAttached(attached);
    newSubmit.setTaskId(task);
    submitRepository.save(newSubmit);

    submitDto.setAttached(attached);
    return submitDto;
  }

  @Override
  public boolean deleteSubmit(String id) {
    Submit submit = submitRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Submit ID not found"));
    submitRepository.delete(submit);
    return true;
  }

  @Override
  public Resource loadSubmitFile(String id) {
    Submit submit = submitRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Submit ID not found"));
    String fileName = submit.getAttached();
    try {
      String uploadDir = "documents/submit/";
      String filePath = uploadDir + fileName;
      Resource resource = new FileSystemResource(filePath);
      if (resource.exists()) {
        return resource;
      } else {
        throw new RuntimeException("Failed to load attached file: " + fileName);
      }
    } catch (Exception e) {
      throw new RuntimeException("Failed to load attached file: " + fileName, e);
    }
  }

  @Override
  public String getSubmitFileUrl(String id) {
    String dowloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
        .path("/documents/download/")
        .path(id)
        .toUriString();
    return dowloadUrl;
  }

  @Override
  public List<Submit> getAllSubmits() {
    List<Submit> submits = (List<Submit>) submitRepository.findAll();
    return submits;
  }

}
