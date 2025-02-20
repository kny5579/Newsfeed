package com.example.newsfeed.repository.board;

import com.example.newsfeed.entity.board.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("SELECT b FROM Board b WHERE b.updatedAt >= :updateAtStart")
    Page<Board> findByUpdateAtAfter(LocalDateTime updateAtStart, Pageable pageable);

    @Query("SELECT b FROM Board b WHERE b.updatedAt <= :updateAtEnd")
    Page<Board> findByUpdateAtBefore(LocalDateTime updateAtEnd, Pageable pageable);

    @Query("SELECT b FROM Board b WHERE b.updatedAt BETWEEN :updateAtStart AND :updateAtEnd")
    Page<Board> findByUpdateAtBetween(LocalDateTime updateAtStart, LocalDateTime updateAtEnd, Pageable pageable);

    Page<Board> findAllByUserId(Long id, Pageable pageable);
}
