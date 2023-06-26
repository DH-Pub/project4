package com.aptech.proj4.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.Authentication;
import com.aptech.proj4.dto.TaskDto;
import com.aptech.proj4.service.TaskService;

@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    TaskService taskService;

    @PostMapping(path = "/create", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })

    ResponseEntity<?> createTask(
            @RequestPart(value = "files", required = false) MultipartFile[] files,
            @RequestPart("task") TaskDto taskDto,
            Authentication creatingUser) {
        try {
            if (taskService.createTask(files, taskDto, creatingUser.getPrincipal().toString())) {
                return ResponseEntity.ok("Add new task successfully");
            }
            else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fail to add new task! Please try again!");
            }
        } 
        catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } 
        catch (Exception ex) {
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
