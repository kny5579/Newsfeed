package com.example.newsfeed.service.user;

import com.example.newsfeed.common.config.PasswordEncoder;
import com.example.newsfeed.common.exception.InvalidCredentialException;
import com.example.newsfeed.common.exception.InvalidPasswordFormatException;
import com.example.newsfeed.common.exception.SamePasswordException;
import com.example.newsfeed.common.exception.NotFoundException;
import com.example.newsfeed.common.utill.JwtUtil;
import com.example.newsfeed.dto.user.req.*;
import com.example.newsfeed.dto.user.res.SignInResponseDto;
import com.example.newsfeed.dto.user.res.UserProfileResponseDto;
import com.example.newsfeed.dto.user.res.UserResponseDto;
import com.example.newsfeed.entity.user.User;
import com.example.newsfeed.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    @Transactional
    public UserResponseDto save(SignUpRequestDto dto) {
        // 이메일 중복 확인
        if (userRepository.existsByEmail(dto.getEmail())) {
            User existingUser = userRepository.findByEmail(dto.getEmail())
                    .orElseThrow(() -> new IllegalArgumentException("해당 이메일은 이미 사용중입니다."));

            // 탈퇴한 회원인지 확인
            if (existingUser.getDeleted()) {
                throw new IllegalArgumentException("탈퇴한 회원은 재가입할 수 없습니다.");
            }
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

    @Transactional(readOnly = true)
    public UserResponseDto findOne(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getImgUrl(),
                user.getEmail(),
                user.getDeleted(),
                user.getCreatedAt(),
                user.getUpdatedAt());
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
                () -> new NotFoundException("사용자를 찾을 수 없습니다.")
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

    public void delete(Long userId, DeleteRequestDto dto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("사용자를 찾을 수 없습니다.")
        );

        // 기존 비밀번호 검증
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new InvalidCredentialException("현재 비밀번호가 일치하지 않습니다.");
        }

        // deleted 상태 변경
        user.setDeleted(true);
        user.setDeletedAt(LocalDateTime.now());

        userRepository.save(user);
    }

    //프로필 관리
    @Transactional
    public void updateProfile(Long id, UserProfileRequestDto dto) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new NotFoundException("사용자를 찾을 수 없습니다.")
        );

        // 비밀번호 변경 시, 기존 비밀번호 검증
        if (dto.getOldPassword() != null) {
            if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
                throw new InvalidCredentialException("현재 비밀번호가 일치하지 않습니다.");
            }

            // 새 비밀번호가 현재 비밀번호와 동일한지 체크
            if (dto.getNewPassword() != null && passwordEncoder.matches(dto.getOldPassword(), dto.getNewPassword())) {
                throw new SamePasswordException("새 비밀번호는 현재 비밀번호와 동일할 수 없습니다.");
            }

            // 비밀번호 형식 검증 (예시: 최소 8자, 대소문자, 숫자, 특수문자 포함)
            if (!isValidPassword(dto.getNewPassword())) {
                throw new InvalidPasswordFormatException("비밀번호는 최소 8자 이상, 대소문자, 숫자, 특수문자를 포함해야 합니다.");
            }

            // 비밀번호를 새 비밀번호로 변경
            user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        }

        // 이메일, 이름, 이미지 등의 다른 프로필 수정
        if (dto.getName() != null) {
            user.setName(dto.getName());
        }
        if (dto.getImgUrl() != null) {
            user.setImgUrl(dto.getImgUrl());
        }

        // 변경된 정보 저장
        userRepository.save(user);
    }

    // 비밀번호 형식 검증 메서드
    private boolean isValidPassword(String password) {
        return password.length() >= 8
                && password.matches(".*[A-Z].*") // 대문자
                && password.matches(".*[a-z].*") // 소문자
                && password.matches(".*\\d.*") // 숫자
                && password.matches(".*[!@#$%^&*(),.?\":{}|<>].*"); // 특수문자
    }

    public UserProfileResponseDto getProfile(Long id) {
        //사용자가 존재하는지 확인
        User user = userRepository.findById(id).orElseThrow(
                () -> new NotFoundException("사용자를 찾을 수 없습니다."));
        //민감한 정보 제외한 프로필 반환(비밀번호 제외)
        return new UserProfileResponseDto(
                user.getId(),
                user.getName(),
                user.getImgUrl(),
                user.getEmail()
        );
    }
}
