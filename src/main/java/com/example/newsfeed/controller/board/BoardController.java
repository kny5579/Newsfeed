package com.example.newsfeed.controller.board;

import com.example.newsfeed.common.consts.OrderBy;
import com.example.newsfeed.common.utill.JwtUtil;
import com.example.newsfeed.dto.boardDto.request.BoardSaveRequestDto;
import com.example.newsfeed.dto.boardDto.request.UpdateBoardRequestDto;
import com.example.newsfeed.dto.boardDto.response.*;
import com.example.newsfeed.service.board.BoardService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<Void> save(
            @RequestHeader("Authorization") String token,
            @ModelAttribute @Valid BoardSaveRequestDto dto
    ) {

        Long loginedId = jwtUtil.getValidatedUserId(token);
        boardService.save(dto, loginedId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<BoardsResponseDto>> findAll(
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size,
            @RequestParam(defaultValue = "UPDATED_AT") OrderBy orderBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction,
            @RequestParam(required = false) LocalDate updateAtStart,
            @RequestParam(required = false) LocalDate updateAtEnd
    ) {

        Page<BoardsResponseDto> pages = boardService.findAll(page, size, orderBy, direction, updateAtStart, updateAtEnd);

        return new ResponseEntity<>(pages, HttpStatus.OK);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResponseDto> find(@PathVariable @Min(1) Long boardId) {

        return new ResponseEntity<>(boardService.find(boardId), HttpStatus.OK);
    }

    @PatchMapping("/{boardId}")
    public ResponseEntity<Void> update(
            @RequestHeader("Authorization") String token,
            @PathVariable @Min(1) Long boardId,
            @Valid @ModelAttribute UpdateBoardRequestDto dto) {

        Long loginedId = jwtUtil.getValidatedUserId(token);
        boardService.update(boardId, loginedId, dto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> delete(
            @RequestHeader("Authorization") String token,
            @PathVariable @Min(1) Long boardId) {

        Long loginedId = jwtUtil.getValidatedUserId(token);
        boardService.delete(boardId, loginedId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/board/{userId}")
    public ResponseEntity<Page<UserBoardFeedResponseDto>> findUserIdFeed(
            @PathVariable @Min(1) Long userId,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size) {

        return new ResponseEntity<>(boardService.findUserIdFeed(userId, page, size), HttpStatus.OK);
    }

    @PostMapping("/{boardId}/likes")
    public ResponseEntity<Void> like(
            @RequestHeader("Authorization") String token,
            @PathVariable @Min(1) Long boardId) {

        Long loginedId = jwtUtil.getValidatedUserId(token);

        boardService.likes(boardId, loginedId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{boardId}/likes")
    public ResponseEntity<Page<LikeUsersDto>> likeList(@PathVariable @Min(1) Long boardId) {

        return new ResponseEntity<>(boardService.likeList(boardId), HttpStatus.OK);
    }
}
