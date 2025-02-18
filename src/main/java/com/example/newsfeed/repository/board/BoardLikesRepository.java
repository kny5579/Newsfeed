package com.example.newsfeed.repository.board;

import com.example.newsfeed.entity.board.BoardLikes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface BoardLikesRepository extends JpaRepository<BoardLikes, Long> {

    Optional<BoardLikes> findByBoardIdAndUserId(Long boardId, Long userId);

    Page<BoardLikes> findByBoardId(Long boardId, Pageable pageable);
}
