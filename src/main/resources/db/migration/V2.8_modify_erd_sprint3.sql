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
    FOREIGN KEY fk_proposal_crew_id (crew_id) REFERENCES crew (id)
);