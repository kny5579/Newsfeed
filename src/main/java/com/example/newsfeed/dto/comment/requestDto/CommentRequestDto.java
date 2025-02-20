package com.example.newsfeed.dto.comment.requestDto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentRequestDto {

    @NotBlank(message = "댓글 내용은 필수 입력값입니다.")
    private String contents;
}
