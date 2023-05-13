package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Follow;
import com.springboot.blog.entity.User;
import com.springboot.blog.payload.FollowDTO;
import com.springboot.blog.repository.FollowRepository;
import com.springboot.blog.repository.UserRepository;
import com.springboot.blog.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowServiceImpl implements FollowService {
    @Autowired
    private FollowRepository followRepository;
    @Autowired
    private UserRepository userRepository;

    //フォローする
    @Override
    public String followUser(Long followingId, Long followerId) {
        User follower = userRepository.findById(followerId).orElseThrow(() -> new RuntimeException("Follower not found"));
        User following = userRepository.findById(followingId).orElseThrow(() -> new RuntimeException("Following user not found"));
        //followテーブル（中間テーブル）に登録
        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowing(following);
        followRepository.save(follow);
        String message = follow.getFollowing().getUsername()+"さんをフォローしました。";
        return  message;
    }

    //フォローしている人を一覧表示
    @Override
    public List<User> showFollowing(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        List<User> followings = userRepository.getFollowings(userId);
        return followings;
    }

    //フォロワーを一覧表示
    @Override
    public List<User> showFollower(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        List<User> followers = userRepository.getFollowers(userId);
        return followers;
    }
}
