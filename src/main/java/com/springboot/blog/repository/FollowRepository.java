package com.springboot.blog.repository;

import com.springboot.blog.entity.Follow;
import com.springboot.blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
//    @Query("SELECT f.following FROM Follow f WHERE f.follower.id = :userId")
//    List<User> getFollowings(Long userId);
//
//    @Query("SELECT f.follower FROM Follow f WHERE f.following.id = :userId")
//    List<User> getFollowers(Long userId);
}
