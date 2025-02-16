package com.example.newsfeed.entity.follower;

import com.example.newsfeed.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor
public class Follower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    private User follower;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        PENDING, ACCEPTED, REJECTED
    }

    public Follower(User follower, User user, Status status) {
        this.follower= follower;
        this.user = user;
        this.status = status;
    }
}
