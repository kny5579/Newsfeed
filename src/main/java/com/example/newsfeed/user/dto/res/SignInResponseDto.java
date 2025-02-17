package com.example.newsfeed.user.dto.res;

import lombok.Getter;

@Getter
public class SignInResponseDto {
    private final Long id;
    private final String email;
    private final String token;

    public SignInResponseDto(Long id, String email, String token) {
        this.id = id;
        this.email = email;
        this.token = token;
    }
}
