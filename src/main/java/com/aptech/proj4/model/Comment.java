package com.aptech.proj4.model;

import java.io.Serializable;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "comments")
public class Comment implements Serializable {
  @Id
  @Column(name = "id")
  private String id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "task_id") // many to one
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private Task taskId;

  @Column(name = "comments")
  private String comment;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "created_by") // many to one
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private User CreatedBy;

  @CreationTimestamp
  @Column(name = "created_at", columnDefinition = "DATETIME")
  private String createdAt;
}