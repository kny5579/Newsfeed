package com.example.newsfeed.service.user;

import com.example.newsfeed.common.aws.S3Service;
import com.example.newsfeed.common.config.PasswordEncoder;
import com.example.newsfeed.common.exception.InvalidCredentialException;
import com.example.newsfeed.common.exception.NotFoundException;
import com.example.newsfeed.common.utill.JwtUtil;
import com.example.newsfeed.dto.user.req.*;
import com.example.newsfeed.dto.user.res.SignInResponseDto;
import com.example.newsfeed.dto.user.res.UserResponseDto;
import com.example.newsfeed.entity.user.User;
import com.example.newsfeed.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Service s3Service;

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

        // S3에 이미지 업로드
        String imgUrl;
        try {
            imgUrl = s3Service.uploadImage(dto.getImg()); // S3에 이미지 업로드하고 URL 가져오기
        } catch (IOException e) {
            throw new RuntimeException("이미지 업로드 중 오류가 발생했습니다.", e);
        }

        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        User user = new User(dto.getName(), imgUrl, dto.getEmail(), encodedPassword); // imgUrl 설정
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

        // 비밀번호 업데이트 시 처리
        if (dto.getOldPassword() != null && dto.getNewPassword() != null) {
            // 기존 비밀번호 검증
            if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
                throw new InvalidCredentialException("현재 비밀번호가 일치하지 않습니다.");
            }

            // 새 비밀번호가 기존 비밀번호와 동일한지 확인
            if (passwordEncoder.matches(dto.getNewPassword(), user.getPassword())) {
                throw new InvalidCredentialException("현재 비밀번호와 동일한 비밀번호로 변경할 수 없습니다.");
            }

            // 비밀번호 업데이트
            user.setPassword(passwordEncoder.encode(dto.getNewPassword())); // 비밀번호 암호화
        }

        // 프로필 이미지 업데이트
        if (dto.getImg() != null && !dto.getImg().isEmpty()) {
            String imgUrl;
            try {
                imgUrl = s3Service.uploadImage(dto.getImg()); // S3에 이미지 업로드하고 URL 가져오기
                user.setImgUrl(imgUrl); // S3에서 가져온 이미지 URL로 업데이트
            } catch (IOException e) {
                throw new RuntimeException("이미지 업로드 중 오류가 발생했습니다.", e);
            }
        }

        // 사용자 정보 저장
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

}
