package com.example.newsfeed.controller.boardController;

import com.example.newsfeed.common.consts.Const;
import com.example.newsfeed.common.consts.OrderBy;
import com.example.newsfeed.dto.boardDto.request.BoardSaveRequestDto;
import com.example.newsfeed.dto.boardDto.response.BoardResponseDto;
import com.example.newsfeed.dto.boardDto.response.BoardsResponseDto;
import com.example.newsfeed.service.boardService.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<Void> save(@ModelAttribute BoardSaveRequestDto dto, @SessionAttribute(name = Const.LOGIN_USER) Long id){

        boardService.save(dto, id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<BoardsResponseDto>> findAll(
            @SessionAttribute(name = Const.LOGIN_USER) Long id,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "UPDATE_AT") OrderBy orderBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction,
            @RequestParam(required = false) LocalDate updateAtStart,
            @RequestParam(required = false) LocalDate updateAtEnd
            ){

        Page<BoardsResponseDto> pages = boardService.findAll(id, page, size, orderBy, direction, updateAtStart, updateAtEnd);

        return new ResponseEntity<>(pages, HttpStatus.OK);
    }

    public ResponseEntity<BoardResponseDto> find(@PathVariable Long id, @SessionAttribute(name = Const.LOGIN_USER) Long userId){

        return new ResponseEntity<>(boardService.find(id, userId), HttpStatus.OK);
    }
}
