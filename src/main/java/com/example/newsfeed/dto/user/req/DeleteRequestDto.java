package com.example.newsfeed.dto.user.req;

import lombok.Getter;

@Getter
public class DeleteRequestDto {
    private final String password;

    public DeleteRequestDto(String password) {
        this.password = password;
    }
}
