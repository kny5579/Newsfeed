package com.example.newsfeed.repository.comment;

import com.example.newsfeed.dto.comment.CommentLikesDto;
import com.example.newsfeed.entity.comment.CommentLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentLikesRepository extends JpaRepository<CommentLikes, Long> {

    @Query("select new com.example.newsfeed.dto.comment.CommentLikesDto(c.comment.id, count(c)) " +
            "from CommentLikes c " +
            "where c.comment.id in :commentIds " +
            "group by c.comment.id")
    List<CommentLikesDto> countByCommentIds(List<Long> commentIds);
    Optional<CommentLikes> findByCommentIdAndUserId(Long commentId, Long userId);
}
