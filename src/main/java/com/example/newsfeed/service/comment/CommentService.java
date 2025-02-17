package com.example.newsfeed.service.comment;

import com.example.newsfeed.dto.comment.requestDto.CommentRequestDto;
import com.example.newsfeed.dto.comment.responseDto.CommentResponseDto;
import com.example.newsfeed.repository.board.BoardRepository;
import com.example.newsfeed.repository.comment.CommentRepository;
import com.example.newsfeed.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    public CommentResponseDto createComment(Long userId, Long boardId, CommentRequestDto commentRequestDto) {
        return null;
    }

    public CommentResponseDto getCommentByBoard(Long boardId) {
        return null;
    }

    public HttpStatusCode updateComment(Long userId, Long commentId, CommentRequestDto commentRequestDto) {
        return null;
    }

    public void deleteComment(Long userId, Long commentId) {
    }
}
