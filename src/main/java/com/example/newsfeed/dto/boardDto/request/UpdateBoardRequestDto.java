package com.example.newsfeed.dto.boardDto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateBoardRequestDto {

    private final String contents;

    private final String image_url;

}
