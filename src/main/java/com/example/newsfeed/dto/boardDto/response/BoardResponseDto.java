package com.example.newsfeed.dto.boardDto.response;

import com.example.newsfeed.dto.comment.responseDto.CommentResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class BoardResponseDto {
    private String name;
    private String user_image_url;
    private String board_image_url;
    private String contents;
    private int likeCnt;
    private boolean like;
    private LocalDate updatedAt;
}
