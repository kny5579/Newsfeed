package com.example.newsfeed.repository.boardRepository;

import com.example.newsfeed.dto.boardDto.BoardLikesDto;
import com.example.newsfeed.entity.boardEntity.BoardLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardLikesRepository extends JpaRepository<BoardLikes, Long> {

    @Query("select new com.example.newsfeed.dto.boardDto.BoardLikesDto(b.board.id, count(b), " +
            "CASE WHEN SUM(CASE WHEN b.user.id = :userId THEN 1 ELSE 0 END ) > 0 THEN true ELSE  false  END ) " +
            "from BoardLikes b " +
            "where b.board.id in :boardIds " +
            "group by b.board.id")
    List<BoardLikesDto> countByBoardIds(@Param("userId") Long userId, @Param("boardIds") List<Long> boardIds);

    Long countByBoardId(Long boardid);

    Long countByBoardIdAndUserId(Long boardId, Long userId);
}
