package com.example.newsfeed.service.board;

import com.example.newsfeed.common.consts.OrderBy;
import com.example.newsfeed.dto.boardDto.request.BoardSaveRequestDto;
import com.example.newsfeed.dto.boardDto.request.UpdateBoardRequestDto;
import com.example.newsfeed.dto.boardDto.response.BoardResponseDto;
import com.example.newsfeed.dto.boardDto.response.BoardsResponseDto;
import com.example.newsfeed.dto.boardDto.response.UserBoardResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;

public interface BoardService {
    void save(BoardSaveRequestDto dto, Long id);

    Page<BoardsResponseDto> findAll(
            Long id, int page, int size, OrderBy orderBy, Sort.Direction direction, LocalDate updateAtStart, LocalDate updateAtEnd);

    BoardResponseDto find(Long id, Long userId);

    void update(Long id, Long userId, UpdateBoardRequestDto dto);

    void delete(Long id, Long userId);

    Page<UserBoardResponseDto> findUserId(Long id, Long userId);

    void likes(Long boardId, Long userId);
}
