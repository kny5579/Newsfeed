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
            //@SessionAttribute(name = Const.LOGIN_USER) Long id,
            @ModelAttribute BoardSaveRequestDto dto
    ) {

        Long id = 1L;
        boardService.save(dto, id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<BoardsResponseDto>> findAll(
            //@SessionAttribute(name = Const.LOGIN_USER) Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "UPDATED_AT") OrderBy orderBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction,
            @RequestParam(required = false) LocalDate updateAtStart,
            @RequestParam(required = false) LocalDate updateAtEnd
    ) {

        Long userId = 1L;
        Page<BoardsResponseDto> pages = boardService.findAll(userId, page, size, orderBy, direction, updateAtStart, updateAtEnd);

        return new ResponseEntity<>(pages, HttpStatus.OK);
    }

    @GetMapping("/board/{id}")
    public ResponseEntity<BoardResponseDto> find(
            //@SessionAttribute(name = Const.LOGIN_USER) Long userId,
            @PathVariable Long id
    ) {
        Long userId = 1L;
        return new ResponseEntity<>(boardService.find(id, userId), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(
//            @SessionAttribute(name = Const.LOGIN_USER) Long userId,
            @PathVariable Long id,
            @ModelAttribute UpdateBoardRequestDto dto) {

        Long userId = 1L;
        boardService.update(id, userId, dto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
//            @SessionAttribute(name = Const.LOGIN_USER) Long userId,
            @PathVariable Long id) {
        Long userId = 1L;
        boardService.delete(id, userId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Page<UserBoardResponseDto>> findUserId( // 그냥 세션에서 유저 아이디만 받아도 되는데 매팽을 어떻게 할지 때문에 나중에 더 생각하기
                                                                  //@SessionAttribute(name = Const.LOGIN_USER) Long userId,
                                                                  @PathVariable Long id) {

        Long userId = 1L;

        return new ResponseEntity<>(boardService.findUserId(id, userId), HttpStatus.OK);
    }

    @PostMapping("/{boardId}/likes")
    public ResponseEntity<Void> like(//@SessionAttribute(name = Const.LOGIN_USER) Long userId,
                                     @PathVariable Long boardId) {
        Long userId = 1L;

        boardService.likes(boardId, userId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{boardId}/likes")
    public ResponseEntity<Page<LikeUsersDto>> likeList(@PathVariable Long boardId){

        return new ResponseEntity<>(boardService.likeList(boardId), HttpStatus.OK);
    }
}
