package com.example.newsfeed.dto.boardDto.response;

import com.example.newsfeed.dto.boardDto.CommentResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class BoardResponseDto {
    private String name;
    private String user_image_url;
    private String board_image_url;
    private String contents;
    private int likeCnt;
    private Page<CommentResponseDto> comment;
    private boolean like;
    private LocalDate updatedAt;
}
