package com.example.newsfeed.service.comment;

import com.example.newsfeed.common.exception.ForbiddenException;
import com.example.newsfeed.common.exception.NotFoundException;
import com.example.newsfeed.dto.comment.requestDto.CommentRequestDto;
import com.example.newsfeed.dto.comment.responseDto.CommentResponseDto;
import com.example.newsfeed.entity.board.Board;
import com.example.newsfeed.entity.comment.Comment;
import com.example.newsfeed.entity.user.User;
import com.example.newsfeed.repository.board.BoardRepository;
import com.example.newsfeed.repository.comment.CommentRepository;
import com.example.newsfeed.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public CommentResponseDto createComment(Long userId, Long boardId, CommentRequestDto dto) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundException("해당 게시글이 존재하지 않습니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("해당 사용자가 존재하지 않습니다."));

        Comment comment = new Comment(dto.getContents(), board, user);
        commentRepository.save(comment);
        return new CommentResponseDto(
                comment.getId(),
                user.getImgUrl(),
                user.getName(),
                comment.getContents(),
                comment.getCreatedAt()
        );
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDto> getCommentByBoard(Long boardId) {
        List<Comment> comments = commentRepository.findByBoardId(boardId);
        return comments.stream()
                .map(c -> new CommentResponseDto(
                        c.getId(),
                        c.getUser().getImgUrl(),
                        c.getUser().getName(),
                        c.getContents(),
                        c.getUpdatedAt()
                )).toList();
    }

    @Transactional
    public CommentResponseDto updateComment(Long userId, Long commentId, CommentRequestDto dto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("해당 댓글이 존재하지 않습니다."));
        if (!comment.getUser().getId().equals(userId)) {
            throw new ForbiddenException("본인이 작성한 댓글만 수정할 수 있습니다.");
        }
        comment.update(dto.getContents());
        return new CommentResponseDto(
                comment.getId(),
                comment.getUser().getImgUrl(),
                comment.getUser().getName(),
                comment.getContents(),
                comment.getUpdatedAt()
        );
    }

    @Transactional
    public void deleteComment(Long userId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("해당 댓글이 존재하지 않습니다."));
        if (!comment.getUser().getId().equals(userId)) {
            throw new ForbiddenException("본인이 작성한 댓글만 삭제할 수 있습니다.");
        }
        commentRepository.delete(comment);
    }
}
