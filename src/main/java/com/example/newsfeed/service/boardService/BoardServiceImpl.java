package com.example.newsfeed.service.boardService;

import com.example.newsfeed.common.consts.OrderBy;
import com.example.newsfeed.dto.boardDto.CommentLikesDto;
import com.example.newsfeed.dto.boardDto.BoardLikesDto;
import com.example.newsfeed.dto.boardDto.CommentCountDto;
import com.example.newsfeed.dto.boardDto.CommentResponseDto;
import com.example.newsfeed.dto.boardDto.request.BoardSaveRequestDto;
import com.example.newsfeed.dto.boardDto.response.BoardResponseDto;
import com.example.newsfeed.dto.boardDto.response.BoardsResponseDto;
import com.example.newsfeed.entity.boardEntity.Comment;
import com.example.newsfeed.entity.boardEntity.Board;
import com.example.newsfeed.entity.userEntity.User;
import com.example.newsfeed.repository.boardRepository.BoardLikesRepository;
import com.example.newsfeed.repository.boardRepository.CommentLikesRepository;
import com.example.newsfeed.repository.boardRepository.CommentRepository;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final BoardLikesRepository boardLikesRepository;
    private final CommentLikesRepository commentLikesRepository;

    @Override
    @Transactional
    public void save(BoardSaveRequestDto dto, Long id) {

        Board board = new Board(dto.getContents(), dto.getImage_url(), User.fromUserId(id));

        // 피드 저장            저장이 실패할 경우 JPA에서 예외 발생
        Board savedBoard = boardRepository.save(board);

        // 저장 내용 검증
        if(savedBoard.getUser() == null || !savedBoard.getUser().getId().equals(id)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Save failed");
        }
    }

    @Override
    public Page<BoardsResponseDto> findAll(
            Long id, int page, int size,
            OrderBy orderBy, Sort.Direction direction,
            LocalDate updateAtStart, LocalDate updateAtEnd) {

        // 페이징 조건
        System.out.println(orderBy.getFieldName());
        Pageable pageable = PageRequest.of(page-1, size, Sort.by(direction, orderBy.getFieldName()));
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

        // 피드 좋아요 수 조회
        List<BoardLikesDto> boardLikesCount = boardLikesRepository.countByBoardIds(id, boardIds);
        Map<Long, Long> boardLikesCountMap = boardLikesCount.stream()
                .collect(Collectors.toMap(BoardLikesDto::getBoardId, BoardLikesDto::getCount));

        // 피드 좋아요 여부  // 본인 피드에 좋아요 가능한지 알아볼 것
        Map<Long, Boolean> boardLikescheckMap = boardLikesCount.stream()
                .collect(Collectors.toMap(BoardLikesDto::getBoardId, BoardLikesDto::isLike));

        return pages.map(board -> new BoardsResponseDto(
                board.getUser().getName(),
                board.getUser().getImg_url(),
                board.getImage_url(),
                board.getContents(),
                boardLikesCountMap.getOrDefault(board.getId(), 0L).intValue(),
                commentCountMap.getOrDefault(board.getId(), 0L).intValue(),
                boardLikescheckMap.getOrDefault(board.getId(), false),
                board.getUpdatedAt().toLocalDate()
        ));
    }

    @Override
    public BoardResponseDto find(Long id, Long userId) {

        // 피드 검색 후 검증
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id not found"));

        // 내일? 오늘? 유저 비공개 확인하는거 테이블이던 뭐던 추가해야 한다고 말하기

        // 피드 좋아요 수 조회
        Long boardLikeCnt = boardLikesRepository.countByBoardId(id);

        // 해당 피드 좋아요 여부
        Long boardLike = boardLikesRepository.countByBoardIdAndUserId(id, userId);
        boolean isBoardLike = boardLike != null;

        // 해당 피드 댓글 조회
        // 댓글 페이징 조건
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "updatedAt"));
        Page<Comment> pages = commentRepository.findAll(pageable);

        // 댓글 아이디 추출
        List<Long> commentIds = pages.stream().map(Comment::getId).toList();

        // 댓글 좋아요 수 조회
        List<CommentLikesDto> commentLikes = commentLikesRepository.countByCommentIds(id, commentIds);
        Map<Long, Long> commentLikesCountMap = commentLikes.stream()
                .collect(Collectors.toMap(CommentLikesDto::getCommentId, CommentLikesDto::getCount));

        // 댓글 좋아요 여부
        Map<Long, Boolean> commentLikescheckMap = commentLikes.stream()
                .collect(Collectors.toMap(CommentLikesDto::getCommentId, CommentLikesDto::isLike));

        // 댓글 페이징
        Page<CommentResponseDto> commentPages = pages.map(comment -> new CommentResponseDto(
                comment.getUser().getImg_url(),
                comment.getUser().getName(),
                comment.getContents(),
                commentLikesCountMap.getOrDefault(comment.getId(), 0L).intValue(),
                commentLikescheckMap.getOrDefault(comment.getId(), false),
                comment.getUpdatedAt().toLocalDate()
        ));

        return new BoardResponseDto(
                board.getUser().getName(),
                board.getUser().getImg_url(),
                board.getImage_url(),
                board.getContents(),
                boardLikeCnt.intValue(),
                commentPages,
                isBoardLike,
                board.getUpdatedAt().toLocalDate()
        );
    }
}
