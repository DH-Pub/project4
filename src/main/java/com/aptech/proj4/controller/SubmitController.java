package com.aptech.proj4.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aptech.proj4.dto.SubmitDto;
import com.aptech.proj4.dto.TaskDto;
import com.aptech.proj4.model.Submit;
import com.aptech.proj4.service.SubmitService;
import com.aptech.proj4.utils.FileUploadUtil;

@RestController
@RequestMapping("/submit")
public class SubmitController {
  @Autowired
  private SubmitService submitService;

  @PostMapping(path = "/upload", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
  public ResponseEntity<?> uploadFile(
      @RequestPart(value = "attached", required = true) MultipartFile file,
      @RequestPart(value = "submit") SubmitDto submitDto,
      @RequestPart(value = "taskId") TaskDto taskDto) {
    try {
      SubmitDto submit = new SubmitDto();
      if (file != null) {
        String originalFilename = file.getOriginalFilename();
        String randomPrefix = UUID.randomUUID().toString(); // Random code
        String fileName = randomPrefix + "_" + StringUtils.cleanPath(originalFilename);
        submitDto.setAttached(fileName);
        submit = submitService.uploadSubmit(submitDto, taskDto);

        String uploadDir = "documents/submit";
        FileUploadUtil.saveFile(uploadDir, submit.getAttached(), file);
      } else {
        submitDto.setAttached(null);
        submit = submitService.uploadSubmit(submitDto, taskDto);
      }
      return ResponseEntity.ok(submit);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task ID not found.");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not upload file.");
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteSubmit(@PathVariable("id") String id) {
    boolean deleted = submitService.deleteSubmit(id);
    if (deleted) {
      return ResponseEntity.status(HttpStatus.OK).body("Submit deleted successfully");
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping
  public ResponseEntity<List<Submit>> getAllDocuments() {
    List<Submit> submits = submitService.getAllSubmits();
    return ResponseEntity.ok(submits);
  }

  @GetMapping("/download/{id}")
  public ResponseEntity<Resource> downloadDocument(@PathVariable("id") String id) {
    Resource resource = submitService.loadSubmitFile(id);
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
        .body(resource);
  }
}
