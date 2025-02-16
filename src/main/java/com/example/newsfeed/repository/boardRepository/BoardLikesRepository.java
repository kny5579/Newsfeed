package com.example.newsfeed.repository.boardRepository;

import com.example.newsfeed.dto.boardDto.BoardLikesDto;
import com.example.newsfeed.entity.boardEntity.BoardLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardLikesRepository extends JpaRepository<BoardLikes, Long> {

    @Query("select new com.example.newsfeed.dto.boardDto.BoardLikesDto(b.board.id, b.user.id, count(b)) " +
            "from BoardLikes b " +
            "where b.board.id in :boardIds " +
            "group by b.board.id")
    List<BoardLikesDto> countByBoardIds(List<Long> boardIds);

    Long countByBoardId(Long boardid);

    Long countByBoardIdAndUserId(Long boardId, Long userId);
}
