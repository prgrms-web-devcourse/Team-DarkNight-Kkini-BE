## 가게 이미지 URL 컬럼 데이터 타입 변경 varchar(255) -> TEXT
ALTER TABLE store
    MODIFY photo_urls TEXT;