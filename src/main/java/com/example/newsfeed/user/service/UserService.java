package com.example.newsfeed.user.service;

import com.example.newsfeed.common.config.PasswordEncoder;
import com.example.newsfeed.common.exception.InvalidCredentialException;
import com.example.newsfeed.common.utill.JwtUtil;
import com.example.newsfeed.entity.User;
import com.example.newsfeed.user.dto.req.SignInRequestDto;
import com.example.newsfeed.user.dto.req.SignUpRequestDto;
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

        String token = jwtUtil.generateToken(user.getEmail());

        return new SignInResponseDto(user.getId(), user.getEmail(), token);
    }
}
