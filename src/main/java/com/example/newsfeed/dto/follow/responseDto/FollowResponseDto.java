package com.example.newsfeed.dto.follow.responseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
//TODO: follower 이미지 추가
public class FollowResponseDto {
    private Long followerId;
    private String followerName;
    private String imgUrl;
}
