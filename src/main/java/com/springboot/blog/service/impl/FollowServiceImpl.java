package com.springboot.blog.service.impl;

import com.springboot.blog.entity.User;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.repository.UserRepository;
import com.springboot.blog.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class FollowServiceImpl implements FollowService {

    @Autowired
    private UserRepository userRepository;

    //フォローする
    @Override
    public String follow(Long followingId, Long followerId) {
        User follower = userRepository.findById(followerId).orElseThrow(() -> new ResourceNotFoundException("User","id", followerId));
        User following = userRepository.findById(followingId).orElseThrow(() -> new ResourceNotFoundException("User","id", followingId));
        if(follower.getFollowings().contains(following)){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,following.getUsername()+"さんをすでにフォローしています");
        }else {
            follower.getFollowings().add(following);
            userRepository.save(follower);
            String message = following.getUsername() + "さんをフォローしました";
            return message;
        }
    }

    //フォローしている人のユーザーネームを一覧表示
    @Override
    public List<String> showFollowing(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User","id", userId));
        Set<User> followings = user.getFollowings();
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
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User","id", userId));
        Set<User> followers = user.getFollowers();
        List<String> followerUsernames = new ArrayList<>();
        followers.stream()
                .forEach(follower ->
                        followerUsernames.add(follower.getUsername())
                        );
        return followerUsernames;
    }

    @Override
    public String unfollow(Long followingId, Long followerId) {
        User follower = userRepository.findById(followerId).orElseThrow(() -> new ResourceNotFoundException("User","id", followerId));
        User following = userRepository.findById(followingId).orElseThrow(() -> new ResourceNotFoundException("User","id", followingId));
        if(!(follower.getFollowings().contains(following))){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,following.getUsername()+"さんをフォローしていません");
        }else{
            follower.getFollowings().remove(following);
            userRepository.save(follower);
            return following.getUsername()+"のフォローを解除しました";
        }
    }
}
