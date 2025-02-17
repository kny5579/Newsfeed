package com.example.newsfeed.service.board;


public interface CommentService {
    void likes(Long commentId, Long userId);
}
