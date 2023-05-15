package com.springboot.blog.service;

import com.springboot.blog.entity.User;
import com.springboot.blog.payload.FollowDTO;
import org.springframework.stereotype.Service;

import java.util.List;


public interface FollowService {
    String followUser(Long followingId, Long followerId);

    List<String> showFollowing(Long userId);

    List<String> showFollower(Long userId);
}
