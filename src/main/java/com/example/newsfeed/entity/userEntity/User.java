package com.example.newsfeed.entity.userEntity;

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

    private String name;

    @Column(name = "image_url")
    private String img_url;

    private String email;

    private String password;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt = null;

    private User(Long id){
        this.id = id;
    }

    public static User fromUserId(Long id) {
        return new User(id);
    }
}
