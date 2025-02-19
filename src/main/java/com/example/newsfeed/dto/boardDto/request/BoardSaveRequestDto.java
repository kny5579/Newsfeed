package com.example.newsfeed.dto.boardDto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardSaveRequestDto {

    @NotNull
    @Size(max = 500)
    private final String contents;

    @NotNull
    @Size(max = 1000)
    private final String image_url;
}
