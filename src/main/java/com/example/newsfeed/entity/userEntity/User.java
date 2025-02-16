package com.example.newsfeed.entity.userEntity;

import com.example.newsfeed.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "user")
@NoArgsConstructor
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String img_url;

    private String email;

    private String password;

    private boolean deleted = false;

    private User(Long id){
        this.id = id;
    }

    public static User fromUserId(Long id) {
        return new User(id);
    }
}
