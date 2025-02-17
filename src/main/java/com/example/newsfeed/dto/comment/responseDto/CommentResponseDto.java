package com.example.newsfeed.dto.comment.responseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class CommentResponseDto {
    private Long id;
    private String img_url;
    private String name;
    private String contents;
    private int likeCnt;
    private boolean like;
    private LocalDate updatedAt;
}
