package com.example.newsfeed.service.boardService;

import com.example.newsfeed.common.consts.OrderBy;
import com.example.newsfeed.dto.boardDto.request.BoardSaveRequestDto;
import com.example.newsfeed.dto.boardDto.response.BoardsResponseDto;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;

public interface BoardService {
    void save(BoardSaveRequestDto dto, Long id);

    BoardsResponseDto findAll(int page, int size, OrderBy orderBy, Sort.Direction direction, LocalDate updateAtStart, LocalDate updateAtEnd);
}
