# crew table content NOT NULL -> NULL
ALTER TABLE crew
    MODIFY COLUMN content varchar(255);

# crew table add column promise_time
ALTER TABLE crew
    ADD COLUMN promise_time datetime NOT NULL ;
