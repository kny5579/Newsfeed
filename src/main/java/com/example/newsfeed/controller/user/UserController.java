package com.example.newsfeed.user.controller;

import com.example.newsfeed.common.utill.JwtUtil;
import com.example.newsfeed.user.dto.req.DeleteRequestDto;
import com.example.newsfeed.user.dto.req.SignUpRequestDto;
import com.example.newsfeed.user.dto.req.UpdateRequestDto;
import com.example.newsfeed.user.dto.res.SignInResponseDto;
import com.example.newsfeed.user.dto.res.UserResponseDto;
import com.example.newsfeed.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signup(@RequestBody SignUpRequestDto dto) {
        return ResponseEntity.ok(userService.save(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findOne(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(userService.findOne(id));
    }

    @PatchMapping("/update")
    public ResponseEntity<String> update(
            @RequestBody UpdateRequestDto dto,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 필요합니다.");
        }

        String token = authorizationHeader.substring(7); // "Bearer " 제거

        // JWT 검증 및 사용자 ID 추출
        Long userId = jwtUtil.extractUserId(token);
        if (!jwtUtil.validateToken(token, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("유효하지 않은 토큰입니다.");
        }

        // 비밀번호 변경
        userService.update(userId, dto);

        return ResponseEntity.ok("성공적으로 변경되었습니다.");
    }

    @PatchMapping("/delete")
    public ResponseEntity<String> delete(
            @RequestBody DeleteRequestDto dto,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 필요합니다.");
        }

        String token = authorizationHeader.substring(7); // "Bearer " 제거

        // JWT 검증 및 사용자 ID 추출
        Long userId = jwtUtil.extractUserId(token);
        if (!jwtUtil.validateToken(token, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("유효하지 않은 토큰입니다.");
        }

        // delete 상태변경
        userService.delete(userId, dto);

        return ResponseEntity.ok("성공적으로 변경되었습니다.");
    }
}
