# 컬럼명 수정: isRead를 is_read로 변경
ALTER TABLE notification
    CHANGE isRead is_read Boolean;

ALTER TABLE notification
    CHANGE receiver receiver_id bigint;

# receiver_id 인덱스 추가
create index idx_receiver_id on notification (receiver_id);