# User Dummy Data
insert into users (nickname, introduction, profile_img_url, provider, oauth_id)
values ("갓용철", "안녕하세요 인프라 마스터 갓용철입니다.", "https://defaultImg.jpg", "kakao", "test");

insert into users (nickname, introduction, profile_img_url, provider, oauth_id)
values ("갓헤나", "안녕하세요 우테코 헤나입니다.", "https://defaultImg.jpg", "kakao", "test2");

insert into users (nickname, introduction, profile_img_url, provider, oauth_id)
values ("참이슬", "안녕하세요 참 귀여운 이슬입니다.", "https://defaultImg.jpg", "kakao", "test4");

insert into users (nickname, introduction, profile_img_url, provider, oauth_id)
values ("용용선생님", "안녕하세요 용용선생님입니다.", "https://defaultImg.jpg", "kakao", "test6");

insert into users (nickname, introduction, profile_img_url, provider, oauth_id)
values ("노기서", "안녕하세요 노기서 입니다.", "https://defaultImg.jpg", "google", "test8");

insert into users (nickname, introduction, profile_img_url, provider, oauth_id)
values ("김진구", "안녕하세요 김진구입니다.", "https://defaultImg.jpg", "google", "test9");

insert into users (nickname, introduction, profile_img_url, provider, oauth_id)
values ("드립기서", "안녕하세요 드립기서입니다.", "https://defaultImg.jpg", "google", "test10");

insert into users (nickname, introduction, profile_img_url, provider, oauth_id)
values ("짱짱주성", "안녕하세요 짱짱주성입니다.", "https://defaultImg.jpg", "google", "test11");

insert into users (nickname, introduction, profile_img_url, provider, oauth_id)
values ("빛은비", "안녕하세요 빛은비입니다.", "https://defaultImg.jpg", "google", "test12");

insert into users (nickname, introduction, profile_img_url, provider, oauth_id)
values ("모닝콜건우", "안녕하세요 모닝콜건우입니다.", "https://defaultImg.jpg", "kakao", "test13");

insert into users (nickname, introduction, profile_img_url, provider, oauth_id)
values ("이비실", "안녕하세요 이비실입니다.", "https://defaultImg.jpg", "kakao", "test14");

# Store(가게) Dummy Data
INSERT INTO store (location, place_id, place_name, categories, road_address_name, photo_urls, phone_number)
VALUES (ST_GeomFromText('POINT(-147.4654321321 35.75413579)'), '123456789', '담뿍 된장찌개', '음식점 한식', '서울 서초구 서초대로74길 51',
        'https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20200207_268%2F15810535080016cY9H_JPEG%2FIMG_5851.JPG',
        '02-3486-8585');

INSERT INTO store (location, place_id, place_name, categories, road_address_name, photo_urls, phone_number)
VALUES (ST_GeomFromText('POINT(-147.4754321321 36.75413579)'), '223456789', '맥도날드 강남역점', '음식점', '서울 강남구 테헤란로 107',
        'https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20200207_268%2F15810535080016cY9H_JPEG%2FIMG_5851.JPG',
        '070-7017-6865');


# Crew(밥모임) Dummy Data
INSERT INTO crew (store_id, name, location,
                  capacity, status, promise_time, content, category)
VALUES (1, '담뿍 된찌 뿌시는 모임', ST_GeomFromText('POINT(-147.4654321321 35.75413579)'),
        6, 'RECRUITING', '2023-03-06 17:00:00', '퇴근하고 든든하게 한끼 같이 먹어요', 'QUIET');

INSERT INTO crew (store_id, name, location,
                  capacity, status, promise_time, content, category)
VALUES (2, '맥도날드 갈 사람', ST_GeomFromText('POINT(-147.4654321321 35.75413579)'),
        8, 'RECRUITING', '2023-03-08 17:00:00', '가볍게 한끼 같이 먹어요', 'QUIET');


# CrewMember(밥모임원) Dummy data
INSERT INTO crew_member(user_id, crew_id, role)
VALUES (1, 1, 'LEADER');

INSERT INTO crew_member(user_id, crew_id, role)
VALUES (2, 1, 'MEMBER');

INSERT INTO crew_member(user_id, crew_id, role)
VALUES (3, 1, 'BLOCKED');

INSERT INTO crew_member(user_id, crew_id, role)
VALUES (5, 2, 'LEADER');

INSERT INTO crew_member(user_id, crew_id, role)
VALUES (6, 2, 'MEMBER');


# Review Dummy data
# 리더에 대한 리뷰
INSERT INTO review(reviewer, reviewee, crew_id, promise_time, content, manner_point, taste_point)
VALUES (2, 1, 1, '2023-03-08 13:00:00', '맛잘알 인정합니다', 5, 5);

INSERT INTO review(reviewer, reviewee, crew_id, promise_time, manner_point, taste_point)
VALUES (6, 5, 2, '2023-03-08 14:00:00', 5, 3);

# 리더가 아닌 밥모임원에 대한 리뷰
INSERT INTO review(reviewer, reviewee, crew_id, promise_time, content, manner_point)
VALUES (3, 2, 1, '2023-03-08 13:00:00', '다음에 또 보고싶어요~', 5);

INSERT INTO review(reviewer, reviewee, crew_id, promise_time, content, manner_point)
VALUES (5, 6, 2, '2023-03-08 14:00:00', '친절하시네요', 5);
