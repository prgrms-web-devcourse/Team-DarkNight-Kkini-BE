-- init Schema SQL
-- create schema mukvengers
-- use mukvengers

DROP TABLE IF EXISTS comment;
DROP TABLE IF EXISTS post;
DROP TABLE IF EXISTS review;
DROP TABLE IF EXISTS proposal;
DROP TABLE IF EXISTS crew_member;
DROP TABLE IF EXISTS crew;
DROP TABLE IF EXISTS store;
DROP TABLE IF EXISTS users;


CREATE TABLE users
(
    id              bigint       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    nickname        varchar(20)  NOT NULL,
    introduction    varchar(100) NOT NULL,
    profile_img_url varchar(255) NOT NULL,
    taste_score     bigint       NOT NULL DEFAULT 0,
    manner_score    double       NOT NULL DEFAULT 36.5,
    leader_count    int          NOT NULL DEFAULT 0,
    crew_count      int          NOT NULL DEFAULT 0,
    reported_count  int          NOT NULL DEFAULT 0,
    enabled         tinyint      NOT NULL DEFAULT 0,
    provider        varchar(10)  NOT NULL,
    oauth_id        varchar(255) NOT NULL,
    created_at      dateTime     NOT NULL DEFAULT now(),
    updated_at      dateTime     NOT NULL DEFAULT now(),
    deleted         tinyint      NULL     DEFAULT 0
);

CREATE TABLE store
(
    id         bigint          NOT NULL PRIMARY KEY AUTO_INCREMENT,
    location   geometry(point) NOT NULL,
    created_at dateTime        NOT NULL DEFAULT now(),
    updated_at dateTime        NOT NULL DEFAULT now(),
    deleted    tinyint         NOT NULL DEFAULT 0
);

CREATE TABLE crew
(
    id         bigint          NOT NULL PRIMARY KEY AUTO_INCREMENT,
    leader_id  bigint          NOT NULL,
    store_id   bigint          NOT NULL,
    name       varchar(20)     NOT NULL,
    location   geometry(point) NOT NULL,
    capacity   int             NOT NULL DEFAULT 2,
    status     varchar(255)    NOT NULL,
    content    varchar(255)    NOT NULL,
    category   varchar(255)    NOT NULL,
    created_at dateTime        NOT NULL DEFAULT now(),
    updated_at dateTime        NOT NULL DEFAULT now(),
    deleted    tinyint         NOT NULL DEFAULT 0,

    FOREIGN KEY (leader_id) REFERENCES users (id),
    FOREIGN KEY (store_id) REFERENCES store (id)
);

CREATE TABLE proposal
(
    id         bigint       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    user_id    bigint       NOT NULL,
    crew_id    bigint       NOT NULL,
    content    varchar(100) NOT NULL,
    checked    tinyint      NOT NULL DEFAULT 0,
    created_at dateTime     NOT NULL DEFAULT now(),
    updated_at dateTime     NOT NULL DEFAULT now(),
    deleted    tinyint      NOT NULL DEFAULT 0,

    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (crew_id) REFERENCES crew (id)
);

CREATE TABLE crew_member
(
    id         bigint   NOT NULL PRIMARY KEY AUTO_INCREMENT,
    user_id    bigint   NOT NULL,
    crew_id    bigint   NOT NULL,
    blocked    tinyint  NOT NULL DEFAULT 0,
    ready      tinyint  NOT NULL DEFAULT 0,
    created_at dateTime NOT NULL DEFAULT now(),
    updated_at dateTime NOT NULL DEFAULT now(),
    deleted    tinyint  NOT NULL DEFAULT 0,

    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (crew_id) REFERENCES crew (id)
);

CREATE TABLE review
(
    id           bigint       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    reviewer     bigint       NOT NULL,
    reviewee     bigint       NOT NULL,
    store_id     bigint       NOT NULL,
    promise_time dateTime     NOT NULL,
    crew_name    varchar(20)  NOT NULL,
    content      varchar(255) NOT NULL,
    manner_point int          NOT NULL,
    taste_point  int          NULL,
    created_at   dateTime     NOT NULL DEFAULT now(),
    updated_at   dateTime     NOT NULL DEFAULT now(),
    deleted      tinyint      NOT NULL DEFAULT 0,

    FOREIGN KEY (reviewer) REFERENCES users (id),
    FOREIGN KEY (reviewee) REFERENCES users (id),
    FOREIGN KEY (store_id) REFERENCES store (id)
);

CREATE TABLE post
(
    id         bigint       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    leader_id  bigint       NOT NULL,
    crew_id    bigint       NOT NULL,
    content    varchar(500) NOT NULL,
    created_at dateTime     NOT NULL DEFAULT now(),
    updated_at dateTime     NOT NULL DEFAULT now(),
    deleted    tinyint      NOT NULL DEFAULT 0,

    FOREIGN KEY (leader_id) REFERENCES users (id),
    FOREIGN KEY (crew_id) REFERENCES crew (id)
);

CREATE TABLE comment
(
    id                bigint       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    user_id           bigint       NOT NULL,
    post_id           bigint       NOT NULL,
    parent_comment_id bigint       NULL,
    content           varchar(255) NOT NULL,
    created_at        dateTime     NOT NULL DEFAULT now(),
    updated_at        dateTime     NOT NULL DEFAULT now(),
    deleted           tinyint      NOT NULL DEFAULT 0,

    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (post_id) REFERENCES post (id),
    FOREIGN KEY (parent_comment_id) REFERENCES comment (id)
);

