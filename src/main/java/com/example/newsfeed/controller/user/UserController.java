package com.example.newsfeed.controller.user;

import com.example.newsfeed.common.utill.JwtUtil;
import com.example.newsfeed.dto.user.req.DeleteRequestDto;
import com.example.newsfeed.dto.user.req.SignUpRequestDto;
import com.example.newsfeed.dto.user.req.UpdateRequestDto;
import com.example.newsfeed.dto.user.res.UserResponseDto;
import com.example.newsfeed.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signup(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("img") MultipartFile img) {

        SignUpRequestDto dto = new SignUpRequestDto(name, null, email, password, img);

        // 사용자 저장
        return ResponseEntity.ok(userService.save(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findOne(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(userService.findOne(id));
    }

    @PatchMapping("/{id}/update")
    public ResponseEntity<String> update(
            @RequestPart(required = false) String oldPassword,
            @RequestPart(required = false) String newPassword,
            @RequestPart(required = false) MultipartFile img,
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

        // UpdateRequestDto 생성
        UpdateRequestDto dto = new UpdateRequestDto(oldPassword, newPassword, img);

        // 사용자 정보 업데이트
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
