package com.example.newsfeed.repository.boardRepository;

import com.example.newsfeed.entity.boardEntity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
}
