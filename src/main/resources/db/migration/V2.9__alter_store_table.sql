## 가게 평점 삭제
ALTER TABLE store
    DROP COLUMN rating;

ALTER TABLE store
    RENAME COLUMN map_store_id TO place_id;