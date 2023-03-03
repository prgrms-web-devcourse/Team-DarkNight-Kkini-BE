# 가게 이름 필드 추가 ex. '자갈치 곰장어'
ALTER TABLE store
    ADD COLUMN place_name varchar(255) NULL;

# 카테고리 필드 추가  ex. ['음식점', '한식', '해물,생선', '장어'] 배열이 넘어옴
ALTER TABLE store
    ADD COLUMN categories varchar(255) NULL;

# 도로명 주소 필드 추가 ex. '부산 금정구 오시게로28번길 4'
ALTER TABLE store
    ADD COLUMN road_address_name varchar(255) NULL;

# 구글 평점 필드 추가 ex. 3.9
ALTER TABLE store
    ADD COLUMN rating varchar(255) NULL;

# 사진 URL 필드 추가 ex. ['https://~', 'https://~', 'https://~', ...] 배열이 넘어옴
ALTER TABLE store
    ADD COLUMN photo_urls varchar(255) NULL;

# 카카오 URL 필드 추가
ALTER TABLE store
    ADD COLUMN kakao_placeUrl varchar(255) NULL;

# 가게 전화번호 필드 추가
ALTER TABLE store
    ADD COLUMN phone_number varchar(255) NULL;