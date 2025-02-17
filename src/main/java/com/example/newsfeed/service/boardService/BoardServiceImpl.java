package com.example.newsfeed.service.boardService;

import com.example.newsfeed.common.consts.OrderBy;
import com.example.newsfeed.dto.boardDto.CommentLikesDto;
import com.example.newsfeed.dto.boardDto.CommentCountDto;
import com.example.newsfeed.dto.boardDto.CommentResponseDto;
import com.example.newsfeed.dto.boardDto.request.BoardSaveRequestDto;
import com.example.newsfeed.dto.boardDto.request.UpdateBoardRequestDto;
import com.example.newsfeed.dto.boardDto.response.BoardResponseDto;
import com.example.newsfeed.dto.boardDto.response.BoardsResponseDto;
import com.example.newsfeed.dto.boardDto.response.UserBoardFeedResponseDto;
import com.example.newsfeed.dto.boardDto.response.UserBoardResponseDto;
import com.example.newsfeed.entity.boardEntity.BoardLikes;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final BoardLikesRepository boardLikesRepository;
    private final CommentRepository commentRepository;
    private final CommentLikesRepository commentLikesRepository;

    @Override
    @Transactional
    public void save(BoardSaveRequestDto dto, Long id) {

        Board board = new Board(dto.getContents(), dto.getImage_url(), User.fromUserId(id));

        // 피드 저장            저장이 실패할 경우 JPA에서 예외 발생
        Board savedBoard = boardRepository.save(board);

        // 저장 내용 검증
        if (savedBoard.getUser() == null || !savedBoard.getUser().getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Save failed");
        }
    }

    @Override
    public Page<BoardsResponseDto> findAll(
            Long id, int page, int size,
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
                board.getUser().getImg_url(),
                board.getImage_url(),
                board.getContents(),
                board.getLikeCnt().intValue(),
                commentCountMap.getOrDefault(board.getId(), 0L).intValue(),
                board.getUser().getId().equals(board.getId()) || board.getLikeCnt() <= 0,
                board.getUpdatedAt().toLocalDate()
        ));
    }

    @Override
    public BoardResponseDto find(Long id, Long userId) {

        // 피드 검색 후 검증
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id not found"));

        // 내일? 오늘? 유저 비공개 확인하는거 테이블이던 뭐던 추가해야 한다고 말하기

        // 해당 피드 좋아요 여부
        boolean isBoardLike = board.getUser().getId().equals(board.getId()) || board.getLikeCnt() <= 0;

        // 해당 피드 댓글 조회
        // 댓글 페이징 조건
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, OrderBy.UPDATED_AT.getFieldName()));
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
                board.getLikeCnt().intValue(),
                commentPages,
                isBoardLike,
                board.getUpdatedAt().toLocalDate()
        );
    }

    @Override
    @Transactional
    public void update(Long id, Long userId, UpdateBoardRequestDto dto) {

        // 해당 피드 검색
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id not found"));

        // 사용자의 피드인지 검증
        if (!board.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your feed");
        }

        // 피드 수정
        board.save(dto.getContents(), dto.getImage_url());
    }

    @Override
    @Transactional
    public void delete(Long id, Long userId) {

        // 해당 피드 검색
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id not found"));

        // 사용자의 피드인지 검증
        if (!board.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your feed");
        }

        // 피드 삭제
        boardRepository.delete(board);
    }

    @Override
    public Page<UserBoardResponseDto> findUserId(Long id, Long userId) {

        if (!id.equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your id");
        }

        // 페이징 조건
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, OrderBy.UPDATED_AT.getFieldName()));
        Page<Board> pages = boardRepository.findAllByUserId(id, pageable);

        // 피드 아이디 추출
        List<Long> boardIds = pages.stream().map(Board::getId).toList();

        // 댓글 수 조회
        List<CommentCountDto> commentCount = commentRepository.countByBoardIds(boardIds);
        Map<Long, Long> commentCountMap = commentCount.stream()
                .collect(Collectors.toMap(CommentCountDto::getBoardId, CommentCountDto::getCount));

        // 피드 수 조회
        Long totalFeeds = pages.getTotalElements();

        // 팔로워 수 조회  이후 합치고 추가
        int follower = 1;

        // 팔로우 수 조회  이후 합치고 추가
        int follow = 1;

        // 개인 페이지 피드
        Page<UserBoardFeedResponseDto> feeds = pages.map(board -> new UserBoardFeedResponseDto(
                board.getImage_url(),
                board.getLikeCnt().intValue(),
                commentCountMap.getOrDefault(board.getId(), 0L).intValue()
        ));


        return pages.map(board -> new UserBoardResponseDto(
                board.getUser().getName(),
                board.getUser().getImg_url(),
                totalFeeds.intValue(),
                follower,
                follow,
                feeds
        ));
    }

    @Override
    public void likes(Long boardId, Long userId) {

        // 해당 피드인지 조회
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id not found"));

        // 사용자의 피드인지 검증
        if (!board.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "It's your feed");
        }

        // 사용자가 해당 피드에 좋아요를 눌렀는지 조회하고 누르지 않았았으면 좋아요 처리
        Optional<BoardLikes> boardLike = boardLikesRepository.findByBoardIdAndUserId(boardId, userId);

        if(boardLike.isEmpty()){
            boardLikesRepository.save((new BoardLikes(board, board.getUser())));
        }else{
            boardLikesRepository.delete(boardLike.get());
            board.cansle();
        }





    }
}
