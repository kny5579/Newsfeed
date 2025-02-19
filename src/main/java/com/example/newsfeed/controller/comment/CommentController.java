package com.example.newsfeed.controller.comment;

import com.example.newsfeed.common.utill.JwtUtil;
import com.example.newsfeed.dto.comment.requestDto.CommentRequestDto;
import com.example.newsfeed.dto.comment.responseDto.CommentResponseDto;
import com.example.newsfeed.service.comment.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    private final JwtUtil jwtUtil;

    @PostMapping("/boards/{boardId}/comments")
    public ResponseEntity<CommentResponseDto> createComment(
            @RequestHeader("Authorization") String token,
            @PathVariable Long boardId,
            @Valid @RequestBody CommentRequestDto commentRequestDto) {
        Long userId = jwtUtil.getValidatedUserId(token);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.createComment(userId, boardId, commentRequestDto));
    }

    @GetMapping("/boards/{boardId}/comments")
    public ResponseEntity<List<CommentResponseDto>> getCommentByBoard(@PathVariable Long boardId) {
        return new ResponseEntity<>(commentService.getCommentByBoard(boardId), HttpStatus.OK);
    }

    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(@RequestHeader("Authorization") String token,
                                                            @PathVariable Long commentId,
                                                            @Valid @RequestBody CommentRequestDto commentRequestDto) {
        Long userId = jwtUtil.getValidatedUserId(token);
        return new ResponseEntity<>(commentService.updateComment(userId, commentId, commentRequestDto), HttpStatus.OK);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@RequestHeader("Authorization") String token,
                                                @PathVariable Long commentId) {
        Long userId = jwtUtil.getValidatedUserId(token);
        commentService.deleteComment(userId, commentId);
        return new ResponseEntity<>("삭제되었습니다.", HttpStatus.OK);
    }

    @PostMapping("/comments/{commentId}/likes")
    public ResponseEntity<Void> like(@RequestHeader("Authorization") String token,
                                     @PathVariable Long commentId) {
        Long userId = jwtUtil.getValidatedUserId(token);
        commentService.likes(commentId, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
