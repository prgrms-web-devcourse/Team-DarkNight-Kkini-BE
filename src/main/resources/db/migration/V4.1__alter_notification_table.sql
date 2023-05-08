DROP TABLE IF EXISTS notification;

CREATE TABLE notification
(
    id         bigint       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    content    varchar(255) NOT NULL,
    type       varchar(255) NOT NULL,
    isRead     boolean      NOT NULL DEFAULT false,
    receiver   bigint       NOT NULL,
    created_at dateTime     NOT NULL DEFAULT now(),
    updated_at dateTime     NOT NULL DEFAULT now(),
    deleted    boolean      NOT NULL DEFAULT false
);