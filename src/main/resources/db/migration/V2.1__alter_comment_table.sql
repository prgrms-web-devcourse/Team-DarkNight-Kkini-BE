DROP TABLE comment;

CREATE TABLE comment
(
    id         bigint       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    user_id    bigint       NOT NULL,
    crew_id    bigint       NOT NULL,
    content    varchar(255) NOT NULL,
    created_at dateTime     NOT NULL DEFAULT now(),
    updated_at dateTime     NOT NULL DEFAULT now(),
    deleted    boolean      NOT NULL DEFAULT false,

    FOREIGN KEY fk_comment_leader_id (user_id) REFERENCES users (id)
);