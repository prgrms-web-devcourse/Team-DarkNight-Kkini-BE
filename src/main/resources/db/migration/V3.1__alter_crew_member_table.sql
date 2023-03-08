## 모임원 테이블 role -> crew_member_role로 변경
ALTER TABLE crew_member
    RENAME COLUMN role TO crew_member_role;