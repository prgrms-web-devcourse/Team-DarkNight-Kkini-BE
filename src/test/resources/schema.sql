-- init SQL
-- create schema mukvengers
-- use mukvengers;

DROP TABLE IF EXISTS notification;
DROP TABLE IF EXISTS chat;
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
    id              bigint        NOT NULL PRIMARY KEY AUTO_INCREMENT,
    nickname        varchar(20)   NOT NULL,
    introduction    varchar(100)  NOT NULL,
    profile_img_url varchar(255)  NOT NULL,
    taste_score     bigint        NOT NULL DEFAULT 0,
    manner_score    decimal(3, 1) NOT NULL DEFAULT 36.5,
    leader_count    int           NOT NULL DEFAULT 0,
    crew_count      int           NOT NULL DEFAULT 0,
    reported_count  int           NOT NULL DEFAULT 0,
    enabled         boolean       NOT NULL DEFAULT false,
    provider        varchar(10)   NOT NULL,
    oauth_id        varchar(255)  NOT NULL,
    created_at      dateTime      NOT NULL DEFAULT now(),
    updated_at      dateTime      NOT NULL DEFAULT now(),
    deleted         boolean       NOT NULL DEFAULT false,

    UNIQUE index_in_provider_and_oauth_id (oauth_id, provider)
);

CREATE TABLE store
(
    id                bigint       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    location          point        NOT NULL SRID 5179,
    place_id          varchar(255) NOT NULL,
    place_name        varchar(255) NULL,
    categories        varchar(255) NULL,
    road_address_name varchar(255) NULL,
    photo_urls        TEXT         NULL,
    kakao_place_url   varchar(255) NULL,
    phone_number      varchar(255) NULL,
    created_at        dateTime     NOT NULL DEFAULT now(),
    updated_at        dateTime     NOT NULL DEFAULT now(),
    deleted           boolean      NOT NULL DEFAULT false,

    UNIQUE KEY uk_store_place_id (place_id)
);

CREATE TABLE crew
(
    id           bigint       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    store_id     bigint       NOT NULL,
    name         varchar(20)  NOT NULL,
    location     point        NOT NULL SRID 5179,
    capacity     int          NOT NULL DEFAULT 2,
    status       varchar(255) NOT NULL,
    promise_time dateTime     NOT NULL,
    content      varchar(255) NULL,
    category     varchar(255) NOT NULL,
    created_at   dateTime     NOT NULL DEFAULT now(),
    updated_at   dateTime     NOT NULL DEFAULT now(),
    deleted      boolean      NOT NULL DEFAULT false,

    FOREIGN KEY fk_crew_store_id (store_id) REFERENCES store (id),
    SPATIAL INDEX spatial_index_in_crew (location),
    INDEX index_in_status (status)
);

CREATE TABLE crew_member
(
    id               bigint      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    user_id          bigint      NOT NULL,
    crew_id          bigint      NOT NULL,
    crew_member_role varchar(50) NOT NULL,
    created_at       dateTime    NOT NULL DEFAULT now(),
    updated_at       dateTime    NOT NULL DEFAULT now(),
    deleted          boolean     NOT NULL DEFAULT false,

    FOREIGN KEY fk_crew_member_crew_id (crew_id) REFERENCES crew (id)
);

CREATE TABLE review
(
    id           bigint       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    reviewer_id  bigint       NOT NULL,
    reviewee_id  bigint       NOT NULL,
    crew_id      bigint       NOT NULL,
    promise_time dateTime     NOT NULL,
    content      varchar(255) NULL,
    manner_score int          NOT NULL,
    taste_score  int          NOT NULL DEFAULT 0,
    created_at   dateTime     NOT NULL DEFAULT now(),
    updated_at   dateTime     NOT NULL DEFAULT now(),
    deleted      boolean      NOT NULL DEFAULT false,

    FOREIGN KEY fk_review_reviewer (reviewer_id) REFERENCES users (id),
    FOREIGN KEY fk_review_reviewee (reviewee_id) REFERENCES users (id),
    FOREIGN KEY fk_review_crew_id (crew_id) REFERENCES crew (id)
);

CREATE TABLE proposal
(
    id         bigint       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    user_id    bigint       NOT NULL,
    leader_id  bigint       NOT NULL,
    crew_id    bigint       NOT NULL,
    content    varchar(100) NOT NULL,
    status     varchar(100) NOT NULL,
    created_at dateTime     NOT NULL DEFAULT now(),
    updated_at dateTime     NOT NULL DEFAULT now(),
    deleted    boolean      NOT NULL DEFAULT false,

    FOREIGN KEY fk_proposal_user_id (user_id) REFERENCES users (id),
    FOREIGN KEY fk_proposal_crew_id (crew_id) REFERENCES crew (id),
    INDEX index_in_leader_id (leader_id)
);

CREATE TABLE chat
(
    id         bigint       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    user_id    bigint       NOT NULL,
    crew_id    bigint       NOT NULL,
    type       varchar(255) NOT NULL,
    content    varchar(255) NOT NULL,
    created_at dateTime     NOT NULL DEFAULT now(),
    updated_at dateTime     NOT NULL DEFAULT now(),
    deleted    boolean      NOT NULL DEFAULT false,

    INDEX idx_crew_id (crew_id)
);

CREATE TABLE notification
(
    id          bigint       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    content     varchar(255) NOT NULL,
    type        varchar(255) NOT NULL,
    is_read     boolean      NOT NULL DEFAULT false,
    receiver_id bigint       NOT NULL,
    created_at  dateTime     NOT NULL DEFAULT now(),
    updated_at  dateTime     NOT NULL DEFAULT now(),
    deleted     boolean      NOT NULL DEFAULT false,

    INDEX idx_receiver_id (receiver_id)
);

