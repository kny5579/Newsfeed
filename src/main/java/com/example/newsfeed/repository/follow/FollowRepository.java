package com.example.newsfeed.repository.follow;

import com.example.newsfeed.entity.follow.Follow;
import com.example.newsfeed.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findByUserId(Long userId);

    Optional<Follow> findByUserAndFollower(User user, User follower);
}
