CREATE TABLE user
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    image_url VARCHAR(1000),
    email VARCHAR(30) NOT NULL UNIQUE,
    password VARCHAR(20) NOT NULL,
    created_at DATETIME,
    updated_at DATETIME,
    deleted_at DATETIME
);

CREATE TABLE board
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    contents MEDIUMTEXT NOT NULL,
    image_url VARCHAR(1000) NOT NULL,
    created_at DATETIME,
    updated_at DATETIME,
    user_id BIGINT,
    foreign key (user_id) references user(id) ON DELETE CASCADE
);

CREATE TABLE follow
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    follower_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    status ENUM('PENDING','ACCEPTED','REJECTED') DEFAULT 'PENDING',
    foreign key (follower_id) references user(id) ON DELETE CASCADE,
    foreign key (user_id) references user(id) ON DELETE CASCADE,
    UNIQUE KEY unique_follow (follower_id, user_id)
);

CREATE TABLE comment
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    contents VARCHAR(200) NOT NULL,
    created_at DATETIME,
    updated_at DATETIME,
    user_id BIGINT,
    board_id BIGINT,
    foreign key (user_id) references user(id) ON DELETE CASCADE,
    foreign key (board_id) references board(id) ON DELETE CASCADE
);

CREATE TABLE comment_likes
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    comment_id BIGINT,
    foreign key (user_id) references user(id) ON DELETE CASCADE,
    foreign key (comment_id) references comment(id) ON DELETE CASCADE
);

CREATE TABLE board_likes
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    board_id BIGINT,
    foreign key (user_id) references user(id) ON DELETE CASCADE,
    foreign key (board_id) references board(id) ON DELETE CASCADE
);