package com.aptech.proj4.model;

import java.io.Serializable;

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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.sql.Date;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "milestone")
public class Milestone implements Serializable {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "[from]")
    private String from;

    @Column(name = "[to]")
    private String to;

    @ManyToOne
    @JoinColumn(name = "projects_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Project project;

}
