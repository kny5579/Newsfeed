package com.example.newsfeed.dto.user.req;

import lombok.Getter;

@Getter
public class UserProfileRequestDto {
    private String name; //사용자 이름
    private String imgUrl; //프로필 이미지 url
    private String email; //이메일(수정 시 필요)
    private String oldPassword; //현재 비밀번호(비밀번호 변경 시 사용)
    private String newPassword; //새 비밀번호(비밀번호 변경 시 사용)

    public UserProfileRequestDto(String name, String imgUrl, String email, String oldPassword, String newPassword) {
        this.name = name;
        this.imgUrl = imgUrl;
        this.email = email;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
}

