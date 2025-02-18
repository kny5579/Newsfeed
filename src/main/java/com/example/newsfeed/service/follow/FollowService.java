package com.example.newsfeed.service.follow;

import com.example.newsfeed.common.exception.BadRequestException;
import com.example.newsfeed.common.exception.ConflictException;
import com.example.newsfeed.common.exception.NotFoundException;
import com.example.newsfeed.dto.follow.responseDto.FollowResponseDto;
import com.example.newsfeed.entity.follow.Follow;
import com.example.newsfeed.entity.user.User;
import com.example.newsfeed.repository.follow.FollowRepository;
import com.example.newsfeed.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.example.newsfeed.entity.follow.Follow.Status.*;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Transactional
    public String followUser(Long userId, Long followerId) {

        if (userId.equals(followerId)) throw new ConflictException("자기 자신은 친구 추가할 수 없습니다.");

        User user = getUserById(userId);
        User follower = getUserById(followerId);

        //이미 친구 추가 요청 보낸 상태인지 확인
        Optional<Follow> existingFollow = followRepository.findByUserAndFollower(user, follower);
        if (existingFollow.isPresent() && existingFollow.get().getStatus() != null) {
            throw new ConflictException("이미 친구 요청이 전송된 적 있는 상대입니다.");
        }

        Follow follow = new Follow(follower, user, PENDING);
        followRepository.save(follow);

        return follow.getFollower().getName() + "님에게 친구 요청을 보냈습니다.";
    }

    @Transactional(readOnly = true)
    public List<FollowResponseDto> getFollowers(Long userId) {
        User user = getUserById(userId);

        return followRepository.findByUserId(userId).stream()
                .filter(follow -> ACCEPTED.equals(follow.getStatus()))
                .map(f -> new FollowResponseDto(
                        f.getFollower().getId(),
                        f.getFollower().getName(),
                        f.getFollower().getImgUrl()))
                .toList();
    }

    @Transactional
    public void acceptFollower(Long userId, Long followerId) {
        User user = getUserById(userId);
        User follower = getUserById(followerId);

        Follow existingFollow = getFollow(user, follower);

        if (!PENDING.equals(existingFollow.getStatus())) {
            throw new BadRequestException("친구 요청 상태가 아닙니다.");
        }
        existingFollow.setStatus(ACCEPTED);
    }

    @Transactional
    public void rejectFollower(Long userId, Long followerId) {
        User user = getUserById(userId);
        User follower = getUserById(followerId);

        Follow existingFollow = getFollow(user, follower);

        if (!PENDING.equals(existingFollow.getStatus())) {
            throw new BadRequestException("친구 요청 상태가 아닙니다.");
        }
        existingFollow.setStatus(REJECTED);
    }

    @Transactional
    public void unfollowUser(Long userId, Long followerId) {
        User user = getUserById(userId);
        User follower = getUserById(followerId);

        Follow existingFollow = getFollow(user, follower);

        if (!ACCEPTED.equals(existingFollow.getStatus())) {
            throw new BadRequestException("친구 상태가 아닙니다.");
        }
        followRepository.delete(existingFollow);
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("해당 사용자를 찾을 수 없습니다."));
    }

    private Follow getFollow(User user, User follower) {
        return followRepository.findByUserAndFollower(user, follower)
                .orElseThrow(() -> new NotFoundException("해당 팔로워를 찾을 수 없습니다."));
    }
}
