package com.example.newsfeed.repository;

import com.example.newsfeed.dto.boardDto.BoardLikesDto;
import com.example.newsfeed.entity.BoardLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardLikesRepository extends JpaRepository<BoardLikes, Long> {

    @Query("select new com.example.newsfeed.dto.boardDto.BoardLikesDto(b.board.id, b.board.user.id,count(b)) " +
            "from BoardLikes b " +
            "where b.board.id in :boardIds " +
            "group by b.board.id")
    List<BoardLikesDto> countByBoardIds(List<Long> boardIds);
}
