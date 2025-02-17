package com.example.newsfeed.user.dto.res;

import lombok.Getter;

@Getter
public class SignInResponseDto {
    private Long id;
    private String email;
    private String token;

    public SignInResponseDto(Long id, String email, String token) {
        this.id = id;
        this.email = email;
        this.token = token;
    }
}
