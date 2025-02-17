package com.example.newsfeed.controller.comment;

import com.example.newsfeed.dto.comment.requestDto.CommentRequestDto;
import com.example.newsfeed.dto.comment.responseDto.CommentResponseDto;
import com.example.newsfeed.repository.comment.CommentRepository;
import com.example.newsfeed.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/boards/{boardId}/comments")
    public ResponseEntity<CommentResponseDto> createComment(@SessionAttribute Long userId,
                                                            @PathVariable Long boardId,
                                                            CommentRequestDto commentRequestDto) {
        return new ResponseEntity<>(commentService.createComment(userId, boardId, commentRequestDto), HttpStatus.CREATED);
    }

    @GetMapping("/boards/{boardId}/comments")
    public ResponseEntity<List<CommentResponseDto>> getCommentByBoard(@PathVariable Long boardId) {
        return new ResponseEntity<>(commentService.getCommentByBoard(boardId), HttpStatus.OK);
    }

    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(@SessionAttribute Long userId,
                                                            @PathVariable Long commentId,
                                                            CommentRequestDto commentRequestDto) {
        return new ResponseEntity<>(commentService.updateComment(userId, commentId, commentRequestDto),HttpStatus.OK);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@SessionAttribute Long userId, @PathVariable Long commentId) {
        commentService.deleteComment(userId, commentId);
        return new ResponseEntity<>("삭제되었습니다.", HttpStatus.OK);
    }
}
