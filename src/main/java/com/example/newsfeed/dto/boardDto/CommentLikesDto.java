package com.example.newsfeed.dto.boardDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentLikesDto {
    private final Long commentId;
    private final Long count;
    private final boolean like;
}
