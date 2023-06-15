package com.aptech.proj4.model;

import java.io.Serializable;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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

    @ManyToOne
    @JoinColumn(name = "project_id") // many to one
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Project project;

    @CreationTimestamp
    @Column(name = "created_at", columnDefinition = "DATETIME")
    private String createdAt;
}
