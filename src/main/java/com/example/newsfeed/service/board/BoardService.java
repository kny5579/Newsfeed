package com.example.newsfeed.service.board;

import com.example.newsfeed.common.consts.OrderBy;
import com.example.newsfeed.dto.boardDto.response.*;
import com.example.newsfeed.dto.comment.CommentCountDto;
import com.example.newsfeed.dto.boardDto.request.BoardSaveRequestDto;
import com.example.newsfeed.dto.boardDto.request.UpdateBoardRequestDto;
import com.example.newsfeed.entity.board.BoardLikes;
import com.example.newsfeed.entity.board.Board;
import com.example.newsfeed.entity.user.User;
import com.example.newsfeed.repository.board.BoardLikesRepository;
import com.example.newsfeed.repository.board.BoardRepository;
import com.example.newsfeed.repository.comment.CommentRepository;
import com.example.newsfeed.repository.user.UserRepository;
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
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardLikesRepository boardLikesRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Transactional
    public void save(BoardSaveRequestDto dto, Long loginedId) {

        Board board = new Board(dto.getContents(), dto.getImage_url(), User.fromUserId(loginedId));

        // 피드 저장            저장이 실패할 경우 JPA에서 예외 발생
        Board savedBoard = boardRepository.save(board);

        // 저장 내용 검증
        if (savedBoard.getUser() == null || !savedBoard.getUser().getId().equals(loginedId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Save failed");
        }
    }

    public Page<BoardsResponseDto> findAll(
            int page, int size,
            OrderBy orderBy, Sort.Direction direction,
            LocalDate updateAtStart, LocalDate updateAtEnd) {

        // 페이징 조건
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(direction, orderBy.getFieldName()));
        Page<Board> pages;

        // 날짜 조건 입력에 따라 페이징
        if (updateAtStart == null && updateAtEnd == null) {          // 둘 다 null
            pages = boardRepository.findAll(pageable);
        } else if (updateAtStart != null && updateAtEnd == null) {   // updateAtEnd만 null
            pages = boardRepository.findByUpdateAtAfter(updateAtStart.atStartOfDay(), pageable);
        } else if (updateAtStart == null && updateAtEnd != null) { // updateAtStart만 null
            pages = boardRepository.findByUpdateAtBefore(updateAtEnd.atStartOfDay(), pageable);
        } else {                                                    // 둘 다 null이 아님
            if (updateAtStart.isAfter(updateAtEnd)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start date is bigger then end date");
            }
            pages = boardRepository.findByUpdateAtBetween(updateAtStart.atStartOfDay(), updateAtEnd.atStartOfDay(), pageable);
        }

        // 피드 아이디 추출
        List<Long> boardIds = pages.stream().map(Board::getId).toList();

        // 댓글 수 조회
        List<CommentCountDto> commentCount = commentRepository.countByBoardIds(boardIds);
        Map<Long, Long> commentCountMap = commentCount.stream()
                .collect(Collectors.toMap(CommentCountDto::getBoardId, CommentCountDto::getCount));

        return pages.map(board -> new BoardsResponseDto(
                board.getUser().getName(),
                board.getUser().getImgUrl(),
                board.getImage_url(),
                board.getContents(),
                board.getLikeCnt().intValue(),
                commentCountMap.getOrDefault(board.getId(), 0L).intValue(),
                board.getUser().getId().equals(board.getId()) || board.getLikeCnt() <= 0,
                board.getUpdatedAt().toLocalDate()
        ));
    }

    public BoardResponseDto find(Long boardId) {

        // 피드 검색 후 검증
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id not found"));

        // 해당 피드 좋아요 여부
        boolean isBoardLike = board.getUser().getId().equals(board.getId()) || board.getLikeCnt() <= 0;

        return new BoardResponseDto(
                board.getUser().getName(),
                board.getUser().getImgUrl(),
                board.getImage_url(),
                board.getContents(),
                board.getLikeCnt().intValue(),
                isBoardLike,
                board.getUpdatedAt().toLocalDate()
        );
    }

    @Transactional
    public void update(Long boardId, Long loginedId, UpdateBoardRequestDto dto) {

        // 해당 피드 확인 후 겁증
        Board board = checkFeed(boardId, loginedId);

        // 피드 수정
        board.save(dto.getContents(), dto.getImage_url());
    }

    @Transactional
    public void delete(Long boardId, Long loginedId) {

        // 해당 피드 확인 후 겁증
        Board board = checkFeed(boardId, loginedId);

        // 피드 삭제
        boardRepository.delete(board);
    }

    public Page<UserBoardFeedResponseDto> findUserIdFeed(Long userId, int page, int size) {

        // 해당 아이디 체크
        if (!userRepository.existsById(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id not found");
        }

        // 페이징 조건
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, OrderBy.UPDATED_AT.getFieldName()));
        Page<Board> pages = boardRepository.findAllByUserId(userId, pageable);

        // 피드 아이디 추출
        List<Long> boardIds = pages.stream().map(Board::getId).toList();

        // 댓글 수 조회
        List<CommentCountDto> commentCount = commentRepository.countByBoardIds(boardIds);
        Map<Long, Long> commentCountMap = commentCount.stream()
                .collect(Collectors.toMap(CommentCountDto::getBoardId, CommentCountDto::getCount));

        // 개인 페이지 피드 목록
        Page<UserBoardFeedResponseDto> feeds = pages.map(board -> new UserBoardFeedResponseDto(
                board.getImage_url(),
                board.getLikeCnt().intValue(),
                commentCountMap.getOrDefault(board.getId(), 0L).intValue()
        ));

        return feeds;
    }


    @Transactional
    public void likes(Long boardId, Long loginedId) {

        // 해당 피드 확인 후 겁증
        Board board = checkFeed(boardId, loginedId);

        // 사용자가 해당 피드에 좋아요를 눌렀는지 조회하고 누르지 않았았으면 좋아요 처리
        Optional<BoardLikes> boardLike = boardLikesRepository.findByBoardIdAndUserId(boardId, loginedId);

        if (boardLike.isEmpty()) {
            boardLikesRepository.save((new BoardLikes(board, board.getUser())));
        } else {
            boardLikesRepository.delete(boardLike.get());
            board.cansle();
        }
    }

    // 해당 상용자가 작성한 피드 출력
    public Page<LikeUsersDto> likeList(Long boardId) {

        // 페이징 기준
        Pageable pageable = PageRequest.of(0, 10);

        // 피드 좋아요 목록
        Page<BoardLikes> likes = boardLikesRepository.findByBoardId(boardId, pageable);

        return likes.map(like -> new LikeUsersDto(
                like.getUser().getId(),
                like.getUser().getName(),
                like.getUser().getImgUrl()
        ));
    }

    // 피드 조회, 본인이 작성 피드인지 검증
    public Board checkFeed(Long boardId, Long loginedId) {

        // 해당 피드 조회
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id not found"));

        // 사용자의 피드인지 검증
        if (!board.getUser().getId().equals(loginedId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "It's your feed");
        }
        return board;
    }
}
