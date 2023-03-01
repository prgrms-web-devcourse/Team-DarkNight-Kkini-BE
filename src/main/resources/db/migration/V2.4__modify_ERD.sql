-- 전체적으로 FK를 제거하여 자유로운 구조를 가질 수 있도록 함

## fk 삭제 "fk_crew_leader_id (leader_id) REFERENCES users (id)"
ALTER TABLE crew
    DROP FOREIGN KEY crew_ibfk_1;

## 방장 아이디 삭제
ALTER TABLE crew
    DROP COLUMN leader_id;

## fk 삭제 "fk_crew_member_user_id (user_id) REFERENCES users (id)"
ALTER TABLE crew_member
    DROP FOREIGN KEY crew_member_ibfk_1;

# 방장 여부 필드 추가
ALTER TABLE crew_member
    ADD COLUMN is_leader boolean NOT NULL DEFAULT false;

# fk 삭제 "fk_comment_leader_id (user_id) REFERENCES users (id)"
ALTER TABLE comment
    DROP FOREIGN KEY comment_ibfk_1;

# 유니크 키 추가(리소스 서버 이름, 리소스 서버에서 관리하는 id)
ALTER TABLE users
    ADD UNIQUE KEY (provider, oauth_id);
