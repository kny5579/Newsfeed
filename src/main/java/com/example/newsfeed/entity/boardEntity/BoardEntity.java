package com.example.newsfeed.entity.boardEntity;

import com.example.newsfeed.common.BaseEntity;
import com.example.newsfeed.entity.userEntity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "user")
@NoArgsConstructor
public class BoardEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cotents;

    private String image_url;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    public void setUser(User user) {
        this.user = user;
    }
}
