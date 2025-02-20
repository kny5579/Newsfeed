package com.example.newsfeed.dto.boardDto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserBoardFeedResponseDto {

    private String imgUrl;
    private int likeCnt;
    private int commentCnt;
}
