package com.springboot.blog.service;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.payload.CommentDTO;

import java.util.List;

public interface CommentService {
    CommentDTO createComment(Long postId, CommentDTO commentDTO);

    List<CommentDTO> getCommentsByPostId(Long postId);

    CommentDTO getCommentsById(Long postId, Long id);

    CommentDTO updateComment(Long postId, Long id, CommentDTO commentDTO);

    void deleteComment(Long postId, Long id);
}
