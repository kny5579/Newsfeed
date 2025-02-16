package com.example.newsfeed.controller.follow;

import com.example.newsfeed.common.consts.Const;
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

    @PostMapping("/{followerId}")
    public ResponseEntity<String> followUser(@SessionAttribute(name = Const.LOGIN_USER) Long userId,
                                             @PathVariable Long followerId) {
        return new ResponseEntity<>(followService.followUser(userId, followerId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<FollowResponseDto>> getFollowers(@SessionAttribute(name = Const.LOGIN_USER) Long userId) {
        return new ResponseEntity<>(followService.getFollowers(userId), HttpStatus.OK);
    }

    @DeleteMapping("/{followerId}")
    public ResponseEntity<String> unfollowUser(@SessionAttribute(name = Const.LOGIN_USER) Long userId,
                                               @PathVariable Long followerId) {
        followService.unfollowUser(userId, followerId);
        return new ResponseEntity<>("삭제되었습니다.", HttpStatus.OK);
    }

}
