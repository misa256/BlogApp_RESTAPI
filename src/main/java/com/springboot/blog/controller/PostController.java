package com.springboot.blog.controller;

import com.springboot.blog.payload.PostDTO;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.service.PostService;
import com.springboot.blog.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
//customize swagger API documentation
@Tag(
        name = "CRUD REST APIs for Post Resource"
)
public class PostController {

    @Autowired
    private PostService postService;

    //create blog post、ADMINユーザーのみ操作可能
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    //swagger UI に認証を追加
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    //customize swagger API documentation
    @Operation(
            summary = "Create Post REST API",
            description = "Create Post REST API is used to save post into DB"
    )
    //customize swagger API documentation
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 created"
    )
    public ResponseEntity<PostDTO> create(@Valid @RequestBody PostDTO postDTO){
        return new ResponseEntity<>(postService.createPost(postDTO), HttpStatus.CREATED);
    }

    //get all posts
    @GetMapping
    //customize swagger API documentation
    @Operation(
            summary = "Get All Post REST API",
            description = "Get All Post REST API is used to get all post from DB"
    )
    //customize swagger API documentation
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 success"
    )
    public PostResponse getAllPosts(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir
    ){
        return postService.getAllPosts(pageNo,pageSize,sortBy,sortDir);
    }

    //get post by id
    @GetMapping("/{id}")
    @Operation(
            summary = "Get Post By Id REST API",
            description = "Get Post By Id REST API is used to get single post from DB"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 success"
    )
    public ResponseEntity<PostDTO> getPostById(@PathVariable(name = "id") long id){
    return ResponseEntity.ok(postService.getPostById(id));
    }

    //update existing post by id、ADMINユーザーのみ操作可能
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @Operation(
            summary = "Update Post REST API",
            description = "Update Post REST API is used to update a particular post in DB"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 success"
    )
    public ResponseEntity<PostDTO> updatePost(@Valid @RequestBody PostDTO postDTO,@PathVariable(name = "id") long id){
    PostDTO postResponse = postService.updatePost(postDTO,id);
    return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }

    //delete post by id、ADMINユーザーのみ操作可能
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @Operation(
            summary = "Delete Post REST API",
            description = "Delete Post REST API is used to delete a particular post in DB"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 success"
    )
    public  ResponseEntity<String> deletePost(@PathVariable(name = "id") long id){
        postService.deletePostById(id);
        return new ResponseEntity<>("Post entity deleted successfully.",HttpStatus.OK);
    }

    //Build get posts by category REST API
    @GetMapping("/category/{id}")
    @Operation(
            summary = "Get Post By CategoryId REST API",
            description = "Get Post By CategoryId REST API is used to get posts in DB"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 success"
    )
    public ResponseEntity<List<PostDTO>> getPostsByCategory(@PathVariable(name = "id") Long categoryId){
        List<PostDTO> postDTOS = postService.getPostsByCategory(categoryId);
        return ResponseEntity.ok(postDTOS);
    }

}
