package com.aptech.proj4.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aptech.proj4.dto.CommentDto;
import com.aptech.proj4.dto.UserDto;
import com.aptech.proj4.model.Comment;
import com.aptech.proj4.model.Task;
import com.aptech.proj4.model.User;
import com.aptech.proj4.repository.CommentRepository;
import com.aptech.proj4.repository.TaskRepository;
import com.aptech.proj4.repository.UserRepository;

@Service
public class CommentServiceImpl implements CommentService {
  @Autowired
  CommentRepository commentRepository;
  @Autowired
  TaskRepository taskRepository;
  @Autowired
  UserRepository userRepository;
  @Autowired
  private ModelMapper modelMapper;

  @Override
  public CommentDto createComment(CommentDto commentDto, String taskId, String userId) {
    Task task = taskRepository.findById(taskId)
        .orElseThrow(() -> new NoSuchElementException("Task ID not found"));

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("User ID not found"));

    Comment comment = new Comment()
        .setId(Long.toString(System.currentTimeMillis()))
        .setTaskId(task)
        .setComment(commentDto.getComment())
        .setCreatedBy(user);

    Comment savedComment = commentRepository.save(comment);

    CommentDto savedCommentDto = new CommentDto();
    savedCommentDto.setId(savedComment.getId());
    savedCommentDto.setTaskId(savedComment.getTaskId().getId());
    savedCommentDto.setComment(savedComment.getComment());
    savedCommentDto.setCreatedBy(savedComment.getCreatedBy().getId());

    return savedCommentDto;
  }

  @Override
  public boolean deleteComment(String id) {
    Comment comment = commentRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("Comment ID not found"));
    commentRepository.delete(comment);
    return true;
  }

  @Override
  public CommentDto updateComment(String id, CommentDto commentDto) {
    Comment existingComment = commentRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("Comment ID not found"));

    existingComment.setComment(commentDto.getComment());
    Comment updatedComment = commentRepository.save(existingComment);

    CommentDto updatedCommentDto = modelMapper.map(updatedComment, CommentDto.class);
    return updatedCommentDto;
  }

  @Override
  public List<Comment> getAllComments() {
    List<Comment> comments = (List<Comment>) commentRepository.findAll();
    return comments;
  }

}
