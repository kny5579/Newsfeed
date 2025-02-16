package com.example.newsfeed.controller.follower;

import com.example.newsfeed.common.consts.Const;
import com.example.newsfeed.dto.follower.responseDto.FollowerResponseDto;
import com.example.newsfeed.service.follower.FollowerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/follows")
@RequiredArgsConstructor
public class FollowerController {

    private final FollowerService followerService;

    @PostMapping
    public ResponseEntity<FollowerResponseDto> followUser(@SessionAttribute(name = Const.LOGIN_USER) Long userId,
                                                          @RequestParam Long followerId) {
        return new ResponseEntity<>(followerService.followUser(userId, followerId), HttpStatus.OK);
    }

}
