package com.springboot.blog.service.impl;

import com.springboot.blog.entity.User;
import com.springboot.blog.repository.UserRepository;
import com.springboot.blog.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FollowServiceImpl implements FollowService {

    @Autowired
    private UserRepository userRepository;

    //フォローする
    @Override
    public String followUser(Long followingId, Long followerId) {
        User follower = userRepository.findById(followerId).orElseThrow(() -> new RuntimeException("Follower not found"));
        User following = userRepository.findById(followingId).orElseThrow(() -> new RuntimeException("Following user not found"));
        follower.getFollowings().add(following);
        userRepository.save(follower);
        String message = following.getUsername()+"さんをフォローしました。";
        return  message;
    }

    //フォローしている人のユーザーネームを一覧表示
    @Override
    public List<String> showFollowing(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        List<User> followings = userRepository.getFollowings(userId);
        List<String> followingUsernames = new ArrayList<>();
        followings.stream()
                .forEach(following ->
                        followingUsernames.add(following.getUsername())
                        );
        return followingUsernames;
    }

    //フォロワーのユーザーネームを一覧表示
    @Override
    public List<String> showFollower(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        List<User> followers = userRepository.getFollowers(userId);
        List<String> followerUsernames = new ArrayList<>();
        followers.stream()
                .forEach(follower ->
                        followerUsernames.add(follower.getUsername())
                        );
        return followerUsernames;
    }
}
