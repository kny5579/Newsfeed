package com.example.newsfeed.dto.boardDto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LikeUsersDto {

    private Long userId;
    private String userName;
    private String userImgUrl;
}
