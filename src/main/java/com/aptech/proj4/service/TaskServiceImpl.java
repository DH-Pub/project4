package com.aptech.proj4.service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.aptech.proj4.dto.SubmitDto;
import com.aptech.proj4.dto.TaskDto;
import com.aptech.proj4.model.Assignee;
import com.aptech.proj4.model.Task;
import com.aptech.proj4.model.User;
import com.aptech.proj4.repository.AssigneeRepository;
import com.aptech.proj4.repository.SubmitRepository;
import com.aptech.proj4.repository.TaskRepository;
import com.aptech.proj4.repository.UserRepository;
import com.aptech.proj4.utils.FileUploadUtil;

@Service
public class TaskServiceImpl implements TaskService {
    public static final String folderPath = "task-files//";
    public static final Path filePath = Paths.get(folderPath);
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AssigneeRepository assigneeRepository;
    @Autowired
    SubmitRepository submitRepository;
    @Autowired
    SubmitService submitService;

    @Override
    public boolean createTask(MultipartFile[] files, TaskDto taskDto, String creatingUser) {
        Task task = new Task();
        SubmitDto submitDto = new SubmitDto();
        List<String> fileNames = new ArrayList<>();

        /* Generate a non-duplicate random string for task.setId() */
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        Set<String> uniqueStrings = new HashSet<>();
        while (sb.length() < 10) {
            int index = random.nextInt(characters.length());
            String randomChar = String.valueOf(characters.charAt(index));
            if (uniqueStrings.add(randomChar)) {
                sb.append(randomChar);
            }
        }

        if (files == null) {
            taskDto.setFiles(null);
        } else {
            Arrays.asList(files).stream().forEach(file -> {
                String fileName = StringUtils.cleanPath(file.getOriginalFilename());
                fileNames.add(fileName);
            });
            taskDto.setFiles(fileNames);
        }
        task.setId(sb.toString())
                .setTaskName(taskDto.getTaskName())
                .setDescription(taskDto.getDescription())
                .setBrief(taskDto.getBrief())
                .setPriority(taskDto.getPriority())
                .setCategory(taskDto.getCategory())
                .setEstimated(taskDto.getEstimated())
                .setActualHours(taskDto.getActualHours())
                .setStartDate(taskDto.getStartDate())
                .setEndDate(taskDto.getEndDate())
                .setDueDate(taskDto.getDueDate())
                .setStatus(taskDto.getStatus())
                .setProject(taskDto.getProject())
                .setPosition(taskDto.getPosition())
                .setFiles(fileNames.toString())
                .setParentTask(taskDto.getParentTask());
        if (taskRepository.save(task) != null) { // If save task successfully
            /* save data into assignees table */
                User assigneeUser = userRepository.findById(taskDto.getUser()).get();
                if (assigneeUser != null) {
                    Assignee assignee = new Assignee();
                    assignee.setTask(task);
                    assignee.setUser(assigneeUser);
                    assignee.setAddedUser(userRepository.findByEmail(creatingUser).get().getId());
                    assigneeRepository.save(assignee);
                }
            // }

            /* save data into submits table */
            if (files != null) {
                String uploadDir = "documents/submit";
                for (MultipartFile file : files) {
                    String originalFilename = file.getOriginalFilename();
                    String randomPrefix = UUID.randomUUID().toString(); // Random code
                    String fileName = randomPrefix + "_" + StringUtils.cleanPath(originalFilename);
                    submitDto.setAttached(fileName);
                    if (submitService.uploadSubmit(submitDto, sb.toString()) != null) {
                        try {
                            FileUploadUtil.saveFile(uploadDir, fileName, file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Task> getAllTasks() {
        List<Task> listTask = (List<Task>) taskRepository.findAll();
        return listTask;
    }

    @Override
    public TaskDto getTaskById(String id, String authentication) {
        TaskDto taskDto = new TaskDto();
        // Task task = taskRepository.findById(id).get();
        // List<String> listStr = new ArrayList<String>();
        // String[] x = task.getFiles().split(",");
        // x[0].replace("[", "");
        // x[x.length - 1].replace("]", "");
        // for (int i=0; i < x.length; i++) {
        // listStr.add(x[i]);
        // }
        // taskDto.setId(task.getId())
        // .setTaskName(task.getTaskName())
        // .setDescription(task.getDescription())
        // .setBrief(task.getBrief())
        // .setPriority(task.getPriority())
        // .setCategory(task.getCategory())
        // .setEstimated(task.getEstimated())
        // .setDueDate(task.getDueDate())
        // .setFiles(listStr)
        // .setStatus(task.getStatus())
        // .setMilestone(task.getMilestone())
        // .setPosition(task.getPosition())
        // .setUsers(task.getUsers());
        return taskDto;
    }
}
