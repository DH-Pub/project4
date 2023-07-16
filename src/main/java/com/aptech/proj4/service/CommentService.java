package com.aptech.proj4.service;

import java.util.List;

import com.aptech.proj4.dto.CommentDto;
import com.aptech.proj4.model.Comment;

public interface CommentService {
  CommentDto createComment(CommentDto comment, String taskId, String userId);

  boolean deleteComment(String id);

  CommentDto updateComment(String id, CommentDto comment);

  List<Comment> getAllComments();

  List<CommentDto> getAllCommentByTaskId(String taskId);
}
