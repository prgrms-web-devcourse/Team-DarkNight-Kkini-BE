# 레디를 삭제하고 신청서를 통해 수락된 인원만 크루 멤버가 된다
ALTER TABLE crew_member
    DROP ready;

# 강퇴는 Role 에서 BLOCKED 로 관리한다
ALTER TABLE crew_member
    DROP blocked;