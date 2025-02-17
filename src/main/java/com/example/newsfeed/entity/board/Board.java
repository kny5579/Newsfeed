package com.example.newsfeed.entity.board;

import com.example.newsfeed.common.entity.BaseEntity;
import com.example.newsfeed.entity.userEntity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "board")
@NoArgsConstructor
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contents;

    private String image_url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Board(String contents, String image_url, User user){
        this.contents = contents;
        this.image_url = image_url;
        this.user = user;
    }

    public void save(String contents, String imageUrl) {
        this.contents = contents;
        this.image_url = imageUrl;
    }
}
