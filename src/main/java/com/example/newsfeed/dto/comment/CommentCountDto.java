package com.example.newsfeed.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentCountDto {

    private final Long boardId;
    private final Long count;
}
