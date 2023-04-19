package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CommentDTO;
import com.springboot.blog.payload.PostDTO;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public CommentDTO createComment(Long postId, CommentDTO commentDTO) {
        Comment comment = mapToEntity(commentDTO);
        //retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post","id",postId));
        //set post to comment entity
        comment.setPost(post);
        //save comment entity to DB
        Comment responseEntity = commentRepository.save(comment);
        return mapToDTO(responseEntity);
    }

    @Override
    public List<CommentDTO> getCommentsByPostId(Long postId) {
        //retrieve comments by postId
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream()
                .map(comment -> mapToDTO(comment))
                .collect(Collectors.toList());
    }

    @Override
    public CommentDTO getCommentsById(Long postId, Long id) {
        //retrieve post from DB
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post","id",postId));
        //retrieve comment by id
        Comment comment = commentRepository.findById(id).orElseThrow(
                ()->new ResourceNotFoundException("Comment","id", id)
        );
        if(!(comment.getPost().getId() == post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }
        return mapToDTO(comment);
    }

    @Override
    public CommentDTO updateComment(Long postId, Long id, CommentDTO commentDTO) {
        //retrieve post from DB
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post","id",postId));
        //retrieve comment by id
        Comment comment = commentRepository.findById(id).orElseThrow(
                ()->new ResourceNotFoundException("Comment","id", id)
        );
        if(!(comment.getPost().getId() == post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }
        //update comment
        comment.setName(commentDTO.getName());
        comment.setEmail(commentDTO.getEmail());
        comment.setBody(commentDTO.getBody());
        //save comment to DB
        Comment updatedComment = commentRepository.save(comment);
        return mapToDTO(updatedComment);
    }

    @Override
    public void deleteComment(Long postId, Long id) {
        //retrieve post from DB
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post","id",postId));
        //retrieve comment by id
        Comment comment = commentRepository.findById(id).orElseThrow(
                ()->new ResourceNotFoundException("Comment","id", id)
        );
        if(!(comment.getPost().getId() == post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }
        //delete comment
        commentRepository.delete(comment);
    }

    //convert entity into DTO
    private CommentDTO mapToDTO(Comment comment) {
        CommentDTO commentDTO = mapper.map(comment, CommentDTO.class);
        return commentDTO;
    }
    //convert DTO into Entity
    private Comment mapToEntity(CommentDTO commentDTO){
        Comment comment = mapper.map(commentDTO, Comment.class);
        return comment;
    }
}
