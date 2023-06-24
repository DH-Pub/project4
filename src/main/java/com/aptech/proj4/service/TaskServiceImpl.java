package com.aptech.proj4.service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.aptech.proj4.dto.TaskDto;
import com.aptech.proj4.model.Assignee;
import com.aptech.proj4.model.Submit;
import com.aptech.proj4.model.Task;
import com.aptech.proj4.model.User;
import com.aptech.proj4.repository.AssigneeRepository;
import com.aptech.proj4.repository.SubmitRepository;
import com.aptech.proj4.repository.TaskRepository;
import com.aptech.proj4.repository.UserRepository;

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

    @Override
    public TaskDto createTask(TaskDto taskDto, String creatingUser) {
        Task task = new Task();
        List<User> users = new ArrayList<User>();
        Submit submit = new Submit();
        List<String> files = taskDto.getFiles();

        taskDto.getUsers().forEach(user -> {
            Optional<User> resUser = userRepository.findById(user.getId());
            User returnUser = resUser.get();
            users.add(returnUser);
        });
        task.setId(taskDto.getId())
            .setTaskName(taskDto.getTaskName())
            .setDescription(taskDto.getDescription())
            .setBrief(taskDto.getBrief())
            .setPriority(taskDto.getPriority())
            .setCategory(taskDto.getCategory())
            .setEstimated(taskDto.getEstimated())
            .setActualHours(taskDto.getActualHours())
            .setStartDate(taskDto.getStartDate())
            .setEndDate(taskDto.getEndDate())
            .setFiles(taskDto.getFiles().toString())
            .setDueDate(taskDto.getDueDate())
            .setStatus(taskDto.getStatus())
            .setMilestone(taskDto.getMilestone())
            .setPosition(taskDto.getPosition());
        taskRepository.save(task);
        for (String file : files) {
            submit.setId(Long.toString(System.currentTimeMillis())).setAttached(file).setTaskId(task);
            submitRepository.save(submit);
        }
        for (User user : users) {
            User assigneeUser = userRepository.findById(user.getId()).orElse(null);
            if (assigneeUser != null) {
                Assignee assignee = new Assignee();
                assignee.setTask(task);
                assignee.setUser(assigneeUser);
                assignee.setAddedUser(userRepository.findByEmail(creatingUser).get().getId());
                assigneeRepository.save(assignee);
            }
        }
        return taskDto;
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
