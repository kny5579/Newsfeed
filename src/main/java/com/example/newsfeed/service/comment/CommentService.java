package com.example.newsfeed.service.comment;

import com.example.newsfeed.repository.board.BoardRepository;
import com.example.newsfeed.repository.comment.CommentRepository;
import com.example.newsfeed.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

}
