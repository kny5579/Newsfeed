package com.example.newsfeed.repository.follower;

import com.example.newsfeed.entity.follower.Follower;
import com.example.newsfeed.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowerRepository extends JpaRepository<Follower, Long> {
    List<Follower> findByUserId(Long userId);
    Optional<Follower> findByUserAndFollower(User user, User follower);
}
