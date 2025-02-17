package com.example.newsfeed.dto.boardDto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@AllArgsConstructor
public class UserBoardResponseDto {

    private String name;
    private String imgUrl;
    private int feedCnt;
    private int followerCnt;
    private int followCnt;
    private Page<UserBoardFeedResponseDto> userBoardFeeds;
}
