package com.example.newsfeed.dto.user.req;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
public class UpdateRequestDto {
    private final String oldPassword;
    private final String newPassword;
    private final MultipartFile img;

    public UpdateRequestDto(String oldPassword, String newPassword, MultipartFile img) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.img = img;
    }
}
