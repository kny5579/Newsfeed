package com.example.newsfeed.controller.user;

import com.example.newsfeed.common.utill.JwtUtil;
import com.example.newsfeed.dto.user.req.SignInRequestDto;
import com.example.newsfeed.dto.user.res.SignInResponseDto;
import com.example.newsfeed.service.user.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class SignInController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signin")
    public ResponseEntity<SignInResponseDto> signIn(
            @RequestBody SignInRequestDto dto,
            HttpServletResponse response) {
        SignInResponseDto loginResponse = userService.handleLogin(dto);

        // JWT 토큰을 쿠키에 저장
        Cookie cookie = new Cookie("JWT_TOKEN", loginResponse.getToken());
        cookie.setHttpOnly(true); // 클라이언트 측 JavaScript에서 접근 불가능
        cookie.setPath("/"); // 쿠키의 경로 설정
        cookie.setMaxAge(60 * 60); // 쿠키 만료 시간
        response.addCookie(cookie); // 쿠키 추가

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/signout")
    public ResponseEntity<String> signOut(
            @RequestHeader("Authorization") String authorizationHeader,
            HttpServletResponse response) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 필요합니다.");
        }

        String token = authorizationHeader.substring(7); // "Bearer " 제거
        Long userId = jwtUtil.extractUserId(token);
        if (!jwtUtil.validateToken(token, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("유효하지 않은 토큰입니다.");
        }

        // 쿠키 삭제
        Cookie cookie = new Cookie("JWT_TOKEN", null); // 쿠키 이름에 맞춰 설정
        cookie.setHttpOnly(true); // 클라이언트에서 접근 불가능
        cookie.setPath("/"); // 쿠키의 경로 설정
        cookie.setMaxAge(0); // 쿠키 만료 시간 설정
        response.addCookie(cookie); // 쿠키 추가

        return ResponseEntity.ok("로그아웃 되었습니다.");
    }
}
