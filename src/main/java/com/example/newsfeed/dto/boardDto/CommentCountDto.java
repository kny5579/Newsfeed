package com.example.newsfeed.dto.boardDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentCountDto {

    private final Long boardId;
    private final Long count;
}
