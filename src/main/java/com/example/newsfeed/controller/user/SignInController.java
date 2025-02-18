package com.example.newsfeed.controller.user;

import com.example.newsfeed.common.utill.JwtUtil;
import com.example.newsfeed.dto.user.req.SignInRequestDto;
import com.example.newsfeed.dto.user.res.SignInResponseDto;
import com.example.newsfeed.service.user.TokenBlacklistService;
import com.example.newsfeed.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class SignInController {
    private final UserService userService;
    private final TokenBlacklistService tokenBlacklistService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signin")
    public ResponseEntity<SignInResponseDto> signIn(@RequestBody SignInRequestDto dto) {
        SignInResponseDto response = userService.handleLogin(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signout")
    public ResponseEntity<String> signOut(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 필요합니다.");
        }

        String token = authorizationHeader.substring(7); // "Bearer " 제거
        Long userId = jwtUtil.extractUserId(token);
        if (!jwtUtil.validateToken(token, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("유효하지 않은 토큰입니다.");
        }

        tokenBlacklistService.blacklistToken(token); // 블랙리스트에 추가

        return ResponseEntity.ok("로그아웃 되었습니다.");
    }
}
