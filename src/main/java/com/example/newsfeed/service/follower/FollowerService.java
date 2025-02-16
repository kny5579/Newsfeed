package com.example.newsfeed.service.follower;

import com.example.newsfeed.dto.follower.responseDto.FollowerResponseDto;
import com.example.newsfeed.entity.follower.Follower;
import com.example.newsfeed.entity.user.User;
import com.example.newsfeed.repository.follower.FollowerRepository;
import com.example.newsfeed.repository.user.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.newsfeed.entity.follower.Follower.Status.PENDING;

@Service
@RequiredArgsConstructor
public class FollowerService {

    private final FollowerRepository followerRepository;
    private final UserRepository userRepository;

    //TODO: 예외처리 클래스 추가 이후 수정
    @Transactional
    public String followUser(Long userId, Long followerId) {

        if(userId.equals(followerId)) throw new RuntimeException("자기 자신은 친구 추가할 수 없습니다.");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("not found"));

        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new RuntimeException("not found"));

        //이미 친구 추가 요청 보낸 상태인지 확인
        Optional<Follower> existingFollow = followerRepository.findByUserAndFollower(user, follower);
        if(existingFollow.isPresent()&&existingFollow.get().getStatus()!=null) {
            throw new RuntimeException("이미 친구 요청이 전송된 적 있는 상대입니다.");
        }

        Follower follow = new Follower(follower, user, PENDING);
        followerRepository.save(follow);

        return follow.getFollower().getName()+"님에게 친구 요청을 보냈습니다.";
    }

    @Transactional(readOnly = true)
    public List<FollowerResponseDto> getFollowers(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("not found"));

        return followerRepository.findByUserId(userId).stream()
                .map(f -> new FollowerResponseDto(f.getFollower().getName()))
                .toList();
    }

}
