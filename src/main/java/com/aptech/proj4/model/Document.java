package com.aptech.proj4.model;

import java.io.Serializable;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table(name = "documents")
public class Document implements Serializable {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "description")
    private String description;

    @Column(name = "files", columnDefinition = "TEXT")
    private String files;

    @Column(name = "project_id")
    private String project_id;

    @CreationTimestamp
    @Column(name = "created_at", columnDefinition = "DATETIME")
    private String createdAt;
}
