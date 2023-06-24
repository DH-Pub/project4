package com.aptech.proj4.service;

import java.util.List;

import com.aptech.proj4.dto.CommentDto;
import com.aptech.proj4.dto.UserDto;
import com.aptech.proj4.model.Comment;

public interface CommentService {
  // CommentDto createComment(CommentDto comment, TaskDto task, UserDto user);

  boolean deleteComment(String id);

  CommentDto updateComment(CommentDto comment);

  List<Comment> getAllComments();
}
