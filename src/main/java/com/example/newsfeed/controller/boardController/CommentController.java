package com.example.newsfeed.controller.boardController;

import com.example.newsfeed.service.board.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/commments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{commentId}/likes")
    public ResponseEntity<Void> like(//@SessionAttribute(name = Const.LOGIN_USER) Long userId,
                                     @PathVariable Long commentId){
        Long userId = 1L;

        commentService.likes(commentId, userId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
