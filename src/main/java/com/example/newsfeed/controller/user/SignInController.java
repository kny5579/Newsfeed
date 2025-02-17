package com.example.newsfeed.user.controller;

import com.example.newsfeed.user.dto.req.SignInRequestDto;
import com.example.newsfeed.user.dto.res.SignInResponseDto;
import com.example.newsfeed.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class SignInController {
    private final UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<SignInResponseDto> signIn(@RequestBody SignInRequestDto dto) {
        SignInResponseDto response = userService.handleLogin(dto);
        return ResponseEntity.ok(response);
    }
}
