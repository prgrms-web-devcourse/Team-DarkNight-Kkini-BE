DROP TABLE IF EXISTS chat;

CREATE TABLE chat
(
    id         bigint       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    user_id    bigint       NOT NULL,
    crew_id    bigint       NOT NULL,
    content    varchar(255) NOT NULL,
    type       varchar(255) NOT NULL,
    created_at dateTime     NOT NULL DEFAULT now(),
    updated_at dateTime     NOT NULL DEFAULT now(),
    deleted    boolean      NOT NULL DEFAULT false
);

create index idx_crew_id on chat (crew_id);