# crew table content NOT NULL -> NULL
ALTER TABLE review
    MODIFY COLUMN content varchar(255);