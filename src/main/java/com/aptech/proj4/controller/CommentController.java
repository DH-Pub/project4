package com.aptech.proj4.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.aptech.proj4.dto.CommentDto;
import com.aptech.proj4.model.Comment;
import com.aptech.proj4.service.CommentService;

@RestController
@RequestMapping("/comment")
public class CommentController {
  @Autowired
  private CommentService commentService;

  @PostMapping("/create")
  public ResponseEntity<?> createdComment(
      @RequestPart(value = "comment", required = true) CommentDto commentDto,
      @RequestPart(value = "taskId") String taskId,
      @RequestPart(value = "userId") String userId) {
    CommentDto savedComment = commentService.createComment(commentDto, taskId, userId);
    return ResponseEntity.ok(savedComment);
  }

  @PutMapping("/{id}")
  public ResponseEntity<CommentDto> updateComment(@PathVariable String id, @RequestBody CommentDto commentDto) {
    CommentDto updatedComment = commentService.updateComment(id, commentDto);
    if (updatedComment != null) {
      return ResponseEntity.ok(updatedComment);
    }
    return ResponseEntity.notFound().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteComment(@PathVariable("id") String id) {
    boolean deleted = commentService.deleteComment(id);
    if (deleted) {
      return ResponseEntity.status(HttpStatus.OK).body("Comment deleted successfully");
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  // @GetMapping
  // public ResponseEntity<List<Comment>> getAllComments() {
  // List<Comment> comments = commentService.getAllComments();
  // return ResponseEntity.ok(comments);
  // }
  @GetMapping("/{taskId}")
  public ResponseEntity<List<CommentDto>> getByTaskId(
      @PathVariable("taskId") String taskId) {
    List<CommentDto> comments = commentService.getAllCommentByTaskId(taskId);
    return ResponseEntity.ok(comments);
  }
}
