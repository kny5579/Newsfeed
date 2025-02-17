package com.example.newsfeed.user.service;

import com.example.newsfeed.common.config.PasswordEncoder;
import com.example.newsfeed.common.exception.InvalidCredentialException;
import com.example.newsfeed.common.exception.UserNotFoundException;
import com.example.newsfeed.common.utill.JwtUtil;
import com.example.newsfeed.entity.User;
import com.example.newsfeed.user.dto.req.SignInRequestDto;
import com.example.newsfeed.user.dto.req.SignUpRequestDto;
import com.example.newsfeed.user.dto.req.UpdateRequestDto;
import com.example.newsfeed.user.dto.res.SignInResponseDto;
import com.example.newsfeed.user.dto.res.UserResponseDto;
import com.example.newsfeed.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    @Transactional
    public UserResponseDto save(SignUpRequestDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("해당 이메일은 이미 사용중입니다.");
        }

        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        User user = new User(dto.getName(), dto.getImgUrl(), dto.getEmail(), encodedPassword);
        userRepository.save(user);
        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getImgUrl(),
                user.getEmail(),
                user.getDeleted(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    public SignInResponseDto handleLogin(SignInRequestDto dto) {
        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(
                () -> new InvalidCredentialException("해당 이메일이 존재하지 않습니다."));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new InvalidCredentialException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtUtil.generateToken(user.getId()); // 사용자 ID를 사용

        return new SignInResponseDto(user.getId(), user.getEmail(), token);
    }

    public void update(Long userId, UpdateRequestDto dto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("사용자를 찾을 수 없습니다.")
        );

        // 기존 비밀번호 검증
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new InvalidCredentialException("현재 비밀번호가 일치하지 않습니다.");
        }

        // 업데이트
        user.setPassword(passwordEncoder.encode(dto.getNewPassword())); // 비밀번호 암호화
        user.setImgUrl(dto.getImgUrl());
        userRepository.save(user);
    }
}
