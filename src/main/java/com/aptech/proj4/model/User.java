package com.aptech.proj4.model;

import java.io.Serializable;

import org.hibernate.annotations.CreationTimestamp;

import com.aptech.proj4.enums.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "users")
public class User implements Serializable {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "email")
    private String email;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "bio")
    private String bio;

    @Column(name = "pic")
    private String pic;

    @CreationTimestamp
    @Column(name = "created_at", columnDefinition = "DATETIME")
    private String createdAt;
}
