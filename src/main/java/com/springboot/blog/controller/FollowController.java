package com.springboot.blog.controller;

import com.springboot.blog.entity.User;
import com.springboot.blog.payload.FollowDTO;
import com.springboot.blog.service.FollowService;
import com.springboot.blog.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/follow")
public class FollowController {
    @Autowired
    private FollowService followService;

    @PostMapping
    public ResponseEntity<String> follow(
            @RequestParam(value = "followingId", required = false) Long followingId,
            @RequestParam(value = "followerId", required = false) Long followerId
    ){
        return new ResponseEntity<>(followService.follow(followingId, followerId), HttpStatus.CREATED);
    }

    @GetMapping("/following/{id}")
    public ResponseEntity<List<String>> showFollowing(@PathVariable(name = "id") long userId){
        return ResponseEntity.ok(followService.showFollowing(userId));
    }

    @GetMapping("/follower/{id}")
    public ResponseEntity<List<String>> showFollower(@PathVariable(name = "id") long userId){
        return ResponseEntity.ok(followService.showFollower(userId));
    }

    @DeleteMapping
    public ResponseEntity<String> unfollow(
            @RequestParam(value = "followingId", required = false) Long followingId,
            @RequestParam(value = "followerId", required = false) Long followerId
    ){
        return ResponseEntity.ok(followService.unfollow(followingId,followerId));

    }
}
