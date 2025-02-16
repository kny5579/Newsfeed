package com.example.newsfeed.service.boardService;

import com.example.newsfeed.common.consts.OrderBy;
import com.example.newsfeed.dto.boardDto.BoardLikesDto;
import com.example.newsfeed.dto.boardDto.CommentCountDto;
import com.example.newsfeed.dto.boardDto.request.BoardSaveRequestDto;
import com.example.newsfeed.dto.boardDto.response.BoardsResponseDto;
import com.example.newsfeed.entity.boardEntity.Board;
import com.example.newsfeed.entity.userEntity.User;
import com.example.newsfeed.repository.BoardLikesRepository;
import com.example.newsfeed.repository.CommentRepository;
import com.example.newsfeed.repository.boardRepository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final BoardLikesRepository board_likesRepository;

    @Override
    @Transactional
    public void save(BoardSaveRequestDto dto, Long id) {

        Board board = new Board(dto.getContents(), dto.getImage_url(), User.fromUserId(id));

        // 피드 저장            저장이 실패할 경우 JPA에서 예외 발생
        Board savedBoard = boardRepository.save(board);

        // 저장 내용 검증
        if(savedBoard.getUser() == null || savedBoard.getUser().getId().equals(id)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Save failed");
        }
    }

    @Override
    public Page<BoardsResponseDto> findAll(
            Long id, int page, int size,
            OrderBy orderBy, Sort.Direction direction,
            LocalDate updateAtStart, LocalDate updateAtEnd) {

        // 페이징 조건
        Pageable pageable = PageRequest.of(page-1, size, Sort.by(direction, orderBy.name().toLowerCase()));
        Page<Board> pages;


        // 날짜 조건 입력에 따라 페이징
        if(updateAtStart == null &&  updateAtEnd == null){          // 둘 다 null
            pages = boardRepository.findAll(pageable);
        } else if(updateAtStart != null &&  updateAtEnd == null){   // updateAtEnd만 null
            pages = boardRepository.findByUpdateAtAfter(updateAtStart.atStartOfDay(), pageable);
        } else if (updateAtStart == null &&  updateAtEnd != null) { // updateAtStart만 null
            pages = boardRepository.findByUpdateAtBefore(updateAtEnd.atStartOfDay(), pageable);
        } else {                                                    // 둘 다 null이 아님
            pages =boardRepository.findByUpdateAtBetween(updateAtStart.atStartOfDay(), updateAtEnd.atStartOfDay(), pageable);
        }

        // 피드 아이디 추출
        List<Long> boardIds = pages.stream().map(Board::getId).toList();

        // 댓글 수 조회
        List<CommentCountDto> commentCount = commentRepository.countByBoardIds(boardIds);
        Map<Long, Long> commentCountMap = commentCount.stream()
                .collect(Collectors.toMap(CommentCountDto::getBoardId, CommentCountDto::getCount));

        // 피드 좋아요 조회
        List<BoardLikesDto> boardLikes = board_likesRepository.countByBoardIds(boardIds);

        // 피드 좋아요 수
        Map<Long, Long> boardLikesCountMap = boardLikes.stream()
                .collect(Collectors.toMap(BoardLikesDto::getBoardId, BoardLikesDto::getCount));

        // 피드 좋아요 여부
        Map<Long, Boolean> boardLikescheckMap = boardLikes.stream()
                .collect(Collectors.toMap(BoardLikesDto::getBoardId, dto -> dto.getUserId().equals(id) && dto.getCount() > 0));

        return pages.map(board -> new BoardsResponseDto(
                board.getUser().getName(),
                board.getUser().getImg_url(),
                board.getImage_url(),
                board.getCotents(),
                boardLikesCountMap.getOrDefault(board.getId(), 0L).intValue(),
                commentCountMap.getOrDefault(board.getId(), 0L).intValue(),
                boardLikescheckMap.get(board.getId()),
                board.getUpdate_at().toLocalDate()
        ));
    }
}
