package com.example.newsfeed.controller.follow;

import com.example.newsfeed.common.consts.Const;
import com.example.newsfeed.common.utill.JwtUtil;
import com.example.newsfeed.dto.follow.responseDto.FollowResponseDto;
import com.example.newsfeed.service.follow.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/follows")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;
    private final JwtUtil jwtUtil;

    @PostMapping("/{followerId}")
    public ResponseEntity<String> followUser(@RequestHeader("Authorization") String token,
                                             @PathVariable Long followerId) {
        Long userId = jwtUtil.getValidatedUserId(token);
        if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return new ResponseEntity<>(followService.followUser(userId, followerId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<FollowResponseDto>> getFollowers(@RequestHeader("Authorization") String token) {
        Long userId = jwtUtil.getValidatedUserId(token);
        if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return new ResponseEntity<>(followService.getFollowers(userId), HttpStatus.OK);
    }

    @PatchMapping("/{followerId}/accept")
    public ResponseEntity<String> acceptFollower(@RequestHeader("Authorization") String token,
                                                 @PathVariable Long followerId) {
        Long userId = jwtUtil.getValidatedUserId(token);
        if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        followService.acceptFollower(userId, followerId);
        return new ResponseEntity<>("요청이 수락되었습니다.", HttpStatus.OK);
    }

    @PatchMapping("/{followerId}/reject")
    public ResponseEntity<String> rejectFollower(@RequestHeader("Authorization") String token,
                                                 @PathVariable Long followerId) {
        Long userId = jwtUtil.getValidatedUserId(token);
        if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        followService.rejectFollower(userId, followerId);
        return new ResponseEntity<>("요청이 거절되었습니다.", HttpStatus.OK);
    }

    @DeleteMapping("/{followerId}")
    public ResponseEntity<String> unfollowUser(@RequestHeader("Authorization") String token,
                                               @PathVariable Long followerId) {
        Long userId = jwtUtil.getValidatedUserId(token);
        if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        followService.unfollowUser(userId, followerId);
        return new ResponseEntity<>("삭제되었습니다.", HttpStatus.OK);
    }

}
