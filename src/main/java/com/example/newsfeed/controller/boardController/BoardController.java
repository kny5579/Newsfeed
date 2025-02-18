package com.example.newsfeed.controller.boardController;

import com.example.newsfeed.common.consts.OrderBy;
import com.example.newsfeed.dto.boardDto.request.BoardSaveRequestDto;
import com.example.newsfeed.dto.boardDto.request.UpdateBoardRequestDto;
import com.example.newsfeed.dto.boardDto.response.BoardResponseDto;
import com.example.newsfeed.dto.boardDto.response.BoardsResponseDto;
import com.example.newsfeed.dto.boardDto.response.LikeUsersDto;
import com.example.newsfeed.dto.boardDto.response.UserBoardResponseDto;
import com.example.newsfeed.service.board.BoardService;
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
    public ResponseEntity<Void> save(
            //@SessionAttribute(name = Const.LOGIN_USER) Long loginedId,
            @ModelAttribute BoardSaveRequestDto dto
    ){
        Long loginedId = 1L;
        boardService.save(dto, loginedId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<BoardsResponseDto>> findAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "UPDATED_AT") OrderBy orderBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction,
            @RequestParam(required = false) LocalDate updateAtStart,
            @RequestParam(required = false) LocalDate updateAtEnd
    ) {

        Page<BoardsResponseDto> pages = boardService.findAll(page, size, orderBy, direction, updateAtStart, updateAtEnd);

        return new ResponseEntity<>(pages, HttpStatus.OK);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResponseDto> find(@PathVariable Long boardId){

        return new ResponseEntity<>(boardService.find(boardId), HttpStatus.OK);
    }

    @PatchMapping("/{boardId}")
    public ResponseEntity<Void> update(
//            @SessionAttribute(name = Const.LOGIN_USER) Long loginedId,
            @PathVariable Long boardId,
            @ModelAttribute UpdateBoardRequestDto dto){

        Long loginedId = 1L;
        boardService.update(boardId, loginedId, dto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> delete(
//            @SessionAttribute(name = Const.LOGIN_USER) Long loginedId,
            @PathVariable Long boardId){
        Long loginedId = 1L;
        boardService.delete(boardId, loginedId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/board/{userId}")
    public ResponseEntity<Page<UserBoardFeedResponseDto>> findUserIdFeed(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size){

        return new ResponseEntity<>(boardService.findUserIdFeed(userId, page, size), HttpStatus.OK);
    }

    @PostMapping("/{boardId}/likes")
    public ResponseEntity<Void> like(//@SessionAttribute(name = Const.LOGIN_USER) Long loginedId,
                                     @PathVariable Long boardId){
        Long loginedId = 1L;

        boardService.likes(boardId, loginedId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{boardId}/likes")
    public ResponseEntity<Page<LikeUsersDto>> likeList(@PathVariable Long boardId){

        return new ResponseEntity<>(boardService.likeList(boardId), HttpStatus.OK);
    }
}
