package com.example.newsfeed.dto.user.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignUpRequestDto {

    @NotBlank
    @Size(max = 4, message = "이름은 4자 이하여야합니다.")
    private final String name;

    private final String imgUrl;

    @NotBlank(message = "이메일이 잘못되었습니다.")
    @Email(message = "이메일 형식을 따르시오.")
    private final String email;

    @NotBlank(message = "패스워드가 잘못되었습니다.")
    @Size(min = 6, message = "패스워드는 6자 이상이여야 합니다.")
    private final String password;

    public SignUpRequestDto(String name, String imgUrl, String email, String password) {
        this.name = name;
        this.imgUrl = imgUrl;
        this.email = email;
        this.password = password;
    }
}
