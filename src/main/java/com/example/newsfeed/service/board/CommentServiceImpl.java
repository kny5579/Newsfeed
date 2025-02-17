package com.example.newsfeed.service.board;

import com.example.newsfeed.entity.board.Comment;
import com.example.newsfeed.entity.board.CommentLikes;
import com.example.newsfeed.repository.board.CommentLikesRepository;
import com.example.newsfeed.repository.board.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final CommentLikesRepository commentLikesRepository;

    @Override
    public void likes(Long commentId, Long userId) {

        // 해당 댓글인지 조회
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id not found"));

        // 사용자의 댓글인지 검증
        if (!comment.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "It's your feed");
        }

        // 사용자가 해당 댓글에 좋아요를 눌렀는지 조회하고 누르지 않았았으면 좋아요 취소 처리
        Optional<CommentLikes> commentLikes = commentLikesRepository.findByCommentIdAndUserId(commentId, userId);

        if(commentLikes.isEmpty()){
            commentLikesRepository.save((new CommentLikes(comment, comment.getUser())));
        }else{
            commentLikesRepository.delete(commentLikes.get());
        }
    }
}
