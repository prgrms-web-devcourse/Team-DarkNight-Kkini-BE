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

    FOREIGN KEY fk_proposal_user_id (user_id) REFERENCES users (id)
);

ALTER TABLE review
    CHANGE reviewer reviewer_id bigint;

ALTER TABLE review
    CHANGE reviewee reviewee_id bigint;

ALTER TABLE review
    CHANGE manner_point manner_score int;

ALTER TABLE review
    CHANGE taste_point taste_score int;