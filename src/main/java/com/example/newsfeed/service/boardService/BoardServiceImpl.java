package com.example.newsfeed.service.boardService;

import com.example.newsfeed.common.consts.OrderBy;
import com.example.newsfeed.dto.boardDto.request.BoardSaveRequestDto;
import com.example.newsfeed.dto.boardDto.response.BoardsResponseDto;
import com.example.newsfeed.entity.boardEntity.Board;
import com.example.newsfeed.entity.userEntity.User;
import com.example.newsfeed.repository.boardRepository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;

    @Override
    public void save(BoardSaveRequestDto dto, Long id) {

        Board board = new Board(dto.getContents(), dto.getImage_url(), User.fromUserId(id));

        Board savedBoard = boardRepository.save(board); // 저장이 실패할 경우 JPA에서 예외 발생

        if(savedBoard.getUser() == null || savedBoard.getUser().getId().equals(id)){    // 저장 내용 검증
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Save failed");
        }
    }

    @Override
    public BoardsResponseDto findAll(
            int page, int size,
            OrderBy orderBy, Sort.Direction direction,
            LocalDate updateAtStart, LocalDate updateAtEnd) {

        Pageable pageable = PageRequest.of(page-1, size, Sort.by(direction, orderBy.name().toLowerCase()));

        Page<Board> pages = boardRepository.findAll(pageable);


        return null;
    }
}
