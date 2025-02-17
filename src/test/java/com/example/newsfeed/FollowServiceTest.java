package com.example.newsfeed.service.follow;

import com.example.newsfeed.dto.follow.responseDto.FollowResponseDto;
import com.example.newsfeed.entity.follow.Follow;
import com.example.newsfeed.entity.userEntity.User;
import com.example.newsfeed.repository.follow.FollowRepository;
import com.example.newsfeed.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.example.newsfeed.entity.follow.Follow.Status.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Mockito 환경 설정
public class FollowServiceTest {

    @Mock
    private FollowRepository followRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FollowService followService;

    private User user;
    private User follower;
    private Follow follow;

    @BeforeEach
    void setUp() {
//        user = new User(1L, "user1");
//        follower = new User(2L, "user2");
        follow = new Follow(follower, user, PENDING);
    }

    @Test
    void testFollowUser_Success() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(follower));
        when(followRepository.findByUserAndFollower(user, follower)).thenReturn(Optional.empty());
        when(followRepository.save(any(Follow.class))).thenReturn(follow);

        // when
        String result = followService.followUser(1L, 2L);

        // then
        assertEquals("user2님에게 친구 요청을 보냈습니다.", result);
        verify(followRepository, times(1)).save(any(Follow.class));
    }

    @Test
    void testFollowUser_AlreadyRequested() {
        // given
        follow.setStatus(PENDING);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(follower));
        when(followRepository.findByUserAndFollower(user, follower)).thenReturn(Optional.of(follow));

        // when & then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> followService.followUser(1L, 2L));
        assertEquals("이미 친구 요청이 전송된 적 있는 상대입니다.", exception.getMessage());
    }

    @Test
    void testFollowUser_SelfFollow() {
        // when & then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> followService.followUser(1L, 1L));
        assertEquals("자기 자신은 친구 추가할 수 없습니다.", exception.getMessage());
    }

    @Test
    void testGetFollowers() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(followRepository.findByUserId(1L)).thenReturn(List.of(follow));

        // when
        List<FollowResponseDto> followers = followService.getFollowers(1L);

        // then
        assertEquals(1, followers.size());
        assertEquals("user2", followers.get(0).getFollowerName());
    }

    @Test
    void testAcceptFollower() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(follower));
        when(followRepository.findByUserAndFollower(user, follower)).thenReturn(Optional.of(follow));

        // when
        followService.acceptFollower(1L, 2L);

        // then
        assertEquals(ACCEPTED, follow.getStatus());
    }

    @Test
    void testRejectFollower() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(follower));
        when(followRepository.findByUserAndFollower(user, follower)).thenReturn(Optional.of(follow));

        // when
        followService.rejectFollower(1L, 2L);

        // then
        assertEquals(REJECTED, follow.getStatus());
    }

    @Test
    void testUnfollowUser() {
        // given
        follow.setStatus(ACCEPTED);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(follower));
        when(followRepository.findByUserAndFollower(user, follower)).thenReturn(Optional.of(follow));

        // when
        followService.unfollowUser(1L, 2L);

        // then
        verify(followRepository, times(1)).delete(follow);
    }

    @Test
    void testUnfollowUser_NotAccepted() {
        // given
        follow.setStatus(PENDING);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(follower));
        when(followRepository.findByUserAndFollower(user, follower)).thenReturn(Optional.of(follow));

        // when & then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> followService.unfollowUser(1L, 2L));
        assertEquals("친구 상태가 아닙니다.", exception.getMessage());
    }
}
