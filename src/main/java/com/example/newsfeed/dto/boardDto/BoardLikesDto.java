package com.example.newsfeed.dto.boardDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardLikesDto {

    private final Long boardId;
    private final Long count;
    private final boolean like;
}
