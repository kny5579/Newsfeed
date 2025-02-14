package com.example.newsfeed.controller.boardController;

import com.example.newsfeed.service.boardService.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
}
