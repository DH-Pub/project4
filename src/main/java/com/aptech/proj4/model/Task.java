package com.aptech.proj4.model;

import java.io.Serializable;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.aptech.proj4.enums.TaskCategory;
import com.aptech.proj4.enums.TaskPriority;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tasks")
public class Task implements Serializable{
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String taskName;

    @Column(name = "description")
    private String description;

    @Column(name = "briefs")
    private String brief;

    @Column(name = "priority")
    private TaskPriority priority;

    @Column(name = "category")
    private TaskCategory category; 

    @Column(name = "estimated")
    private int estimated;

    @Column(name = "actual_hours")
    private int actualHours;

    @Column(name = "start_date")
    private String startDate;

    @Column(name = "end_date")
    private String endDate;

    @Column(name = "due_date")
    private String dueDate;

    @Column(name = "files")
    private String files;

    @Column(name = "status")
    private String status;

    @UpdateTimestamp
    @Column(name = "status_update", columnDefinition = "DATETIME")
    private String statusUpdatedAt;

    @ManyToOne
    @JoinColumn(name = "milestone_id") 
    private Milestone milestone;

    @Column(name = "position")
    private int position;

    @Column(name = "version")
    private String version;

    @UpdateTimestamp
    @Column(name = "updated_at", columnDefinition = "DATETIME")
    private String updatedAt;

    @CreationTimestamp
    @Column(name = "created_at", columnDefinition = "DATETIME")
    private String createAt;

    @ManyToOne
    @JoinTable(
        name = "assignees",
        joinColumns = @JoinColumn(name = "task_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private User users;

    @Column(name = "parent_task")
    private String parentTask;
}
