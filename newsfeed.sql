CREATE TABLE user
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(20) NOT NULL,
    email VARCHAR(30) NOT NULL,
    password VARCHAR(20) NOT NULL,
    created_date DATETIME,
    updated_date DATETIME,
    deleted BOOLEAN
);

CREATE TABLE board
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    contents VARCHAR(200) NOT NULL,
    created_date DATETIME,
    updated_date DATETIME,
    user_id BIGINT,
    foreign key (user_id) references user(id) ON DELETE CASCADE
);

CREATE TABLE follow
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    follower_id BIGINT NOT NULL,
    following_id BIGINT NOT NULL,
    foreign key (follower_id) references user(id) ON DELETE CASCADE,
    foreign key (following_id) references user(id) ON DELETE CASCADE,
    UNIQUE KEY unique_follow (follower_id, following_id)
);

CREATE TABLE comment
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    contents VARCHAR(200) NOT NULL,
    created_date DATETIME,
    updated_date DATETIME,
    user_id BIGINT,
    board_id BIGINT,
    foreign key (user_id) references user(id) ON DELETE CASCADE,
    foreign key (board_id) references board(id) ON DELETE CASCADE
);

CREATE TABLE likes
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    board_id BIGINT NULL,
    comment_id BIGINT NULL,
    foreign key (user_id) references user(id) ON DELETE CASCADE,
    foreign key (board_id) references board(id) ON DELETE CASCADE,
    foreign key (comment_id) references comment(id) ON DELETE CASCADE,
    CONSTRAINT check_like CHECK (     -- 둘 중 하나는 null
        (board_id IS NOT NULL AND comment_id IS NULL) OR
        (board_id IS NULL AND comment_id IS NOT NULL)
        )
);