package com.example.newsfeed.dto.user.res;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserProfileResponseDto {
    private Long id; //사용자 아이디
    private String name; //사용자 이름
    private String imgUrl; //프로필 이미지 URL
    private String email; //사용자 이메일
    private Boolean deleted; //사용자 탈퇴 상태
    private String createdAt; //계정 생성 일시
    private String updatedAt; //프로필 수정 일시

    public UserProfileResponseDto(Long id, String name, String imgUrl, String email) {
        this.id = id;
        this.name = name;
        this.imgUrl = imgUrl;
        this.email = email;
    }
}
