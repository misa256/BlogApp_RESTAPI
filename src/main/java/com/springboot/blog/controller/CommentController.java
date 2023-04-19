package com.springboot.blog.controller;

import com.springboot.blog.payload.CommentDTO;
import com.springboot.blog.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDTO> createComment(
            @PathVariable(name = "postId") Long postId,
            @Valid  @RequestBody CommentDTO commentDTO){
        return new ResponseEntity<>(commentService.createComment(postId,commentDTO), HttpStatus.CREATED);
    }

    @GetMapping("/posts/{postId}/comments")
    public List<CommentDTO> getCommentsByPostId(
            @PathVariable(name = "postId") Long postId
    ){
        return commentService.getCommentsByPostId(postId);
    }

    @GetMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<CommentDTO> getCommentById(
            @PathVariable(name = "postId") Long postId,
            @PathVariable(name = "id") Long id
    ){
    return new ResponseEntity<>(commentService.getCommentsById(postId,id), HttpStatus.OK);
    }

    @PutMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<CommentDTO> updateComments(
            @PathVariable(name = "postId") Long postId,
            @PathVariable(name = "id") Long id,
           @Valid  @RequestBody CommentDTO commentDTO
    ){
      return new ResponseEntity<>(commentService.updateComment(postId,id,commentDTO),HttpStatus.OK);
    }

    @DeleteMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<String> deleteComment(
            @PathVariable(name = "postId") Long postId,
            @PathVariable(name = "id") Long id
    ){
       commentService.deleteComment(postId, id);
       return new ResponseEntity<>("Comment deleted successfully",HttpStatus.OK);
    }
}
