package com.example.newsfeed.dto.user.res;

import lombok.Getter;

@Getter
public class UserProfileResponseDto {
    private Long id; //사용자 아이디
    private String name; //사용자 이름
    private String imgUrl; //프로필 이미지 URL
    private String email; //사용자 이메일

    public UserProfileResponseDto(Long id, String name, String imgUrl, String email) {
        this.id = id;
        this.name = name;
        this.imgUrl = imgUrl;
        this.email = email;
    }
}
