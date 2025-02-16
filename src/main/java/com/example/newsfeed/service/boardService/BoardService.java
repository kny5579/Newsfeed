package com.example.newsfeed.service.boardService;

import com.example.newsfeed.common.consts.OrderBy;
import com.example.newsfeed.dto.boardDto.request.BoardSaveRequestDto;
import com.example.newsfeed.dto.boardDto.response.BoardResponseDto;
import com.example.newsfeed.dto.boardDto.response.BoardsResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;

public interface BoardService {
    void save(BoardSaveRequestDto dto, Long id);

    Page<BoardsResponseDto> findAll(
            Long id, int page, int size, OrderBy orderBy, Sort.Direction direction, LocalDate updateAtStart, LocalDate updateAtEnd);

    BoardResponseDto find(Long id, Long userId);
}
