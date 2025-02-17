package com.example.newsfeed.entity;

import com.example.newsfeed.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "user")
@NoArgsConstructor
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String imgUrl;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean deleted = false;

    private User(Long id) {
        this.id = id;
    }

    public User(String name, String imgUrl, String email, String password) {
        this.name = name;
        this.imgUrl = imgUrl;
        this.email = email;
        this.password = password;
        this.deleted = false;
    }
}
