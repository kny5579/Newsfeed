package com.example.newsfeed.repository.boardRepository;

import com.example.newsfeed.entity.boardEntity.BoardLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface BoardLikesRepository extends JpaRepository<BoardLikes, Long> {

    Optional<BoardLikes> findByBoardIdAndUserId(Long boardId, Long userId);
}
