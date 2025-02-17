package com.example.newsfeed.dto.user.req;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UpdateRequestDto {
    private final String oldPassword;
    private final String newPassword;
    private final String imgUrl;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public UpdateRequestDto(String oldPassword, String newPassword, String imgUrl, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.imgUrl = imgUrl;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
