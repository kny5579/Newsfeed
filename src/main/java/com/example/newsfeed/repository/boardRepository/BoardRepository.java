package com.example.newsfeed.repository.boardRepository;

import com.example.newsfeed.entity.boardEntity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
}
