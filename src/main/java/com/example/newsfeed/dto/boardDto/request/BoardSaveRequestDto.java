package com.example.newsfeed.dto.boardDto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class BoardSaveRequestDto {

    @NotNull
    @Size(max = 500)
    private final String contents;

    @NotNull
    private final MultipartFile image;
}
