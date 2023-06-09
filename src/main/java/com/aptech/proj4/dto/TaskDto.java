package com.aptech.proj4.dto;

import java.util.List;

import com.aptech.proj4.enums.TaskCategory;
import com.aptech.proj4.enums.TaskPriority;
import com.aptech.proj4.model.Project;
import com.aptech.proj4.model.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {
    private String id;
    private String taskName;
    private String description;
    private String brief;
    private TaskPriority priority;
    private TaskCategory category;
    private int estimated;
    private int actualHours;
    private String startDate;
    private String endDate;
    private String dueDate;
    private List<String> files;
    private String status;
    private String statusUpdateAt;
    private Project project;
    private int position;
    private User user;
    private String version;
    private String parentTask;
}
