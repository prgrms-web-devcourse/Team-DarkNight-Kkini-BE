-- 밥 모임 상태 정보에 설정
ALTER TABLE crew
    ADD INDEX index_in_status (status);

ALTER TABLE proposal
    ADD INDEX index_in_leader_id (leader_id);

ALTER TABLE users
    ADD UNIQUE INDEX index_in_provider_and_oauth_id (oauth_id, provider);