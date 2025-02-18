package com.example.newsfeed.controller.user;

import com.example.newsfeed.dto.user.req.ProfileUpdateRequestDto;
import com.example.newsfeed.dto.user.res.UserProfileResponseDto;
import com.example.newsfeed.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class ProfileController {
    private final UserService userService;

    //프로필 조회
    @GetMapping("/users/{userid}")
    public ResponseEntity<UserProfileResponseDto> getProfile(@PathVariable Long id) {
        UserProfileResponseDto profile = userService.getProfile(id); //서비스에서 프로필을 가져오는 메서드 호출
        return ResponseEntity.ok(profile);
    }

    //프로필 수정
    @PatchMapping("/users/update")
    public ResponseEntity<String> updateProfile(@PathVariable Long id, @RequestBody ProfileUpdateRequestDto dto) {
        userService.updateProfile(id, dto); //프로필 수정 로직을 서비스에서 처리
        return ResponseEntity.ok("프로필이 정상적으로 수정되었습니다.");
    }
}
