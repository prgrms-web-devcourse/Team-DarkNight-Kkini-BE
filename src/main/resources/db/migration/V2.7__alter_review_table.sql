# fk 삭제 "FOREIGN KEY fk_review_store_id (store_id) REFERENCES store (id)"
ALTER TABLE review
    DROP FOREIGN KEY review_ibfk_3;

# 컬럼명 수정: store_id를 crew_id로 변경
ALTER TABLE review
    CHANGE store_id crew_id bigint;

# fk 추가
ALTER TABLE review
    ADD FOREIGN KEY(crew_id) REFERENCES crew (id);

# 컬럼 삭제: 외래키로 존재하기 때문에 필요X
ALTER TABLE review
    DROP crew_name;