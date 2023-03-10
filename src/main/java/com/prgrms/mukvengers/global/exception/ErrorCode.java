package com.prgrms.mukvengers.global.exception;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

	// Common
	INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "잘못된 입력 값입니다."),
	INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "C002", "잘못된 타입입니다."),
	MISSING_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C003", "인자가 부족합니다."),
	NOT_EXIST_API(HttpStatus.BAD_REQUEST, "C004", "요청 주소가 올바르지 않습니다."),
	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C005", "사용할 수 없는 메서드입니다."),
	HANDLE_ACCESS_DENIED(HttpStatus.FORBIDDEN, "C006", "접근 권한이 없습니다."),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C007", "서버 에러입니다."),

	// User
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "존재하지 않는 사용자입니다."),

	// Store
	STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "S001", "존재하지 않는 가게입니다."),

	// Crew
	CREW_NOT_FOUND(HttpStatus.NOT_FOUND, "CR001", "존재하지 않는 모임입니다."),

	CREW_ILLEGAL_STATUS(HttpStatus.BAD_REQUEST, "CR002", "올바르지 않는 모임의 상태입니다."),

	// CrewMember
	LEADER_NOT_FOUND(HttpStatus.NOT_FOUND, "CM001", "해당 밥모임에 존재하지 않는 리더입니다."),
	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "CM002", "해당 밥모임에 존재하지 않는 밥모임원입니다."),

	CREW_MEMBER_ILLEGAL_ROLE(HttpStatus.BAD_REQUEST, "CM003", "올바르지 않는 모임원의 권한입니다."),
	// Review
	REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "R001", "존재하지 않는 리뷰입니다."),
	REVIEW_NO_ACCESS(HttpStatus.UNAUTHORIZED, "R002", "해당 리뷰를 볼 수 있는 접근 권한이 없습니다."),

	// Proposal
	PROPOSAL_NOT_FOUND(HttpStatus.NOT_FOUND, "P001", "존재하지 않는 신청서입니다."),
	PROPOSAL_BLOCKED_USER(HttpStatus.FORBIDDEN, "P002", "강퇴된 밥모임에는 신청서를 작성할 수 없습니다."),
	REFUSED_PROPOSAL(HttpStatus.NOT_FOUND, "P003", "작성하신 신청서가 거절되었습니다."),
	INVALID_PROPOSAL_STATUS(HttpStatus.NOT_FOUND, "P004", "올바르지 않은 신청서 응답 상태입니다."),
	
	// Auth
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "A001", "유효하지 않은 토큰입니다."),
	EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "A002", "만료된 토큰입니다."),
	NOT_FOUND_TOKEN(HttpStatus.UNAUTHORIZED, "A003", "로그인이 필요합니다."),
	NOT_FOUND_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "A004", "유효하지 않은 리프레쉬 토큰입니다."),
	NOT_FOUND_COOKIE(HttpStatus.UNAUTHORIZED, "A005", "쿠키를 찾을 수 없습니다.");

	private final HttpStatus status;
	private final String code;
	private final String message;

	ErrorCode(HttpStatus status, String code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}
}
