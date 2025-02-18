package com.example.newsfeed.repository.comment;

import com.example.newsfeed.dto.comment.CommentCountDto;
import com.example.newsfeed.entity.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByBoardId(Long boardId);

    @Query("select new com.example.newsfeed.dto.boardDto.CommentCountDto(c.board.id, count(c)) " +
            "from Comment c " +
            "where c.board.id in :boardIds " +
            "group by c.board.id")
    List<CommentCountDto> countByBoardIds(List<Long> boardIds);
}
