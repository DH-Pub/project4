package com.aptech.proj4.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aptech.proj4.dto.CommentDto;
import com.aptech.proj4.dto.UserDto;
import com.aptech.proj4.model.Comment;
import com.aptech.proj4.repository.CommentRepository;

@Service
public class CommentServiceImpl implements CommentService {
  @Autowired
  CommentRepository commentRepository;
  @Autowired
  private ModelMapper modelMapper;

  // @Override
  // public CommentDto createComment(CommentDto comment, TaskDto task, UserDto
  // user) {
  // // TODO Auto-generated method stub
  // throw new UnsupportedOperationException("Unimplemented method
  // 'createComment'");
  // }

  @Override
  public boolean deleteComment(String id) {
    Comment comment = commentRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("Comment ID not found"));
    commentRepository.delete(comment);
    return true;
  }

  @Override
  public CommentDto updateComment(CommentDto comment) {
    Comment updatedComment = modelMapper.map(comment, Comment.class);
    commentRepository.save(updatedComment);
    return comment;
  }

  @Override
  public List<Comment> getAllComments() {
    List<Comment> comments = (List<Comment>) commentRepository.findAll();
    return comments;
  }

}
