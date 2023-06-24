package com.aptech.proj4.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;

import com.aptech.proj4.dto.TaskDto;
import com.aptech.proj4.dto.UserDto;
import com.aptech.proj4.repository.TaskRepository;
import com.aptech.proj4.service.TaskService;
import com.aptech.proj4.utils.FileUploadUtil;

@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    TaskService taskService;

    @PostMapping(path = "/create", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    
    ResponseEntity<?> createTask(
        @RequestPart(value = "files", required = false) MultipartFile[] files,
        @RequestPart("task") TaskDto taskDto,
        Authentication creatingUser
    ) {
        try {
            TaskDto reqTask = new TaskDto();
            List<String> fileNames = new ArrayList<>();
            if(files != null){
                Arrays.asList(files).stream().forEach(file -> {
                    String fileName = StringUtils.cleanPath(file.getOriginalFilename());
                    fileNames.add(fileName);
                    String uploadDir = "imgs/task-files/";
                    try {
                        FileUploadUtil.saveFile(uploadDir, fileName, file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                taskDto.setFiles(fileNames);
            } else {
                taskDto.setFiles(null);
            }
            return ResponseEntity.ok(taskService.createTask(taskDto, creatingUser.getPrincipal().toString()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping("/getAll")
    ResponseEntity<?> getAllTasks() {
        try {
            return ResponseEntity.ok(taskService.getAllTasks());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PostMapping("/getById")
    ResponseEntity<?> getTaskById(@RequestBody String id, Authentication authentication) {
        try {
            return ResponseEntity.ok(taskService.getTaskById(id, authentication.getPrincipal().toString()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }
}
