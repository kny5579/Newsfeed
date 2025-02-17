package com.example.newsfeed.repository.boardRepository;

import com.example.newsfeed.dto.boardDto.CommentLikesDto;
import com.example.newsfeed.entity.boardEntity.CommentLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentLikesRepository extends JpaRepository <CommentLikes, Long> {

    @Query("select new com.example.newsfeed.dto.boardDto.CommentLikesDto(c.comment.id, count(c), " +
            "CASE WHEN SUM(CASE WHEN c.user.id = :userId THEN 1 ELSE 0 END ) > 0 THEN true ELSE  false  END ) " +
            "from CommentLikes c " +
            "where c.comment.id in :commentIds " +
            "group by c.comment.id")
    List<CommentLikesDto> countByCommentIds(Long userId, List<Long> commentIds);
}
