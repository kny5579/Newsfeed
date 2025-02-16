package com.example.newsfeed.dto.boardDto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class BoardsResponseDto {
    private String name;
    private String user_image_url;
    private String board_image_url;
    private String contents;
    private int likeCnt;
    private int commentCnt;
    private boolean like;
    private LocalDate update_at;
}
