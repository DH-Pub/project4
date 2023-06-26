package com.aptech.proj4.repository;

import org.springframework.data.repository.CrudRepository;

import com.aptech.proj4.model.Comment;

public interface CommentRepository extends CrudRepository<Comment, String> {

}
