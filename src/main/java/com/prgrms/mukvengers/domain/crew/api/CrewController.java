package com.prgrms.mukvengers.domain.crew.api;

import static org.springframework.http.MediaType.*;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.prgrms.mukvengers.domain.crew.dto.request.CreateCrewRequest;
import com.prgrms.mukvengers.domain.crew.dto.request.SearchCrewRequest;
import com.prgrms.mukvengers.domain.crew.dto.request.UpdateStatusRequest;
import com.prgrms.mukvengers.domain.crew.dto.response.CrewPageResponse;
import com.prgrms.mukvengers.domain.crew.dto.response.CrewResponse;
import com.prgrms.mukvengers.domain.crew.dto.response.CrewResponses;
import com.prgrms.mukvengers.domain.crew.facade.CrewFacadeService;
import com.prgrms.mukvengers.domain.crew.dto.response.MyCrewResponse;
import com.prgrms.mukvengers.domain.crew.service.CrewService;
import com.prgrms.mukvengers.global.common.dto.ApiResponse;
import com.prgrms.mukvengers.global.common.dto.IdResponse;
import com.prgrms.mukvengers.global.security.jwt.JwtAuthentication;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/crews")
public class CrewController {

	private final CrewService crewService;
	private final CrewFacadeService crewFacadeService;

	/**
	 * <pre>
	 *     밥 모임 생성
	 * </pre>
	 * @param createCrewRequest 밥 모임 생성 DTO
	 * @param user 유저 정보
	 * @return status : 201, body : 생성된 밥 모임 조회 redirectUri
	 */
	@PostMapping(consumes = APPLICATION_JSON_VALUE)
	public ResponseEntity<IdResponse> create
	(
		@RequestBody @Valid CreateCrewRequest createCrewRequest,
		@AuthenticationPrincipal JwtAuthentication user
	) {
		IdResponse idResponse = crewFacadeService.create(createCrewRequest, user.id());
		URI location = UriComponentsBuilder.fromUriString("/api/v1/crews/" + idResponse.id()).build().toUri();
		return ResponseEntity.created(location).build();
	}

	/**
	 * <pre>
	 *     밥 모임 조회
	 * </pre>
	 * @param crewId 밥 모임 아이디
	 * @return status : 200, body : 조회된 밥 모임 데이터 DTO
	 */
	@GetMapping(value = "/{crewId}", produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponse<CrewResponse>> getById
	(
		@PathVariable Long crewId,
		@AuthenticationPrincipal JwtAuthentication user
	) {
		CrewResponse response = crewService.getById(crewId);
		return ResponseEntity.ok().body(new ApiResponse<>(response));
	}

	@GetMapping(value = "/me", produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponse<MyCrewResponse>> getByUserId(
		@AuthenticationPrincipal JwtAuthentication user
	) {
		MyCrewResponse responses = crewService.getByUserId(user.id());
		return ResponseEntity.ok().body(new ApiResponse<>(responses));
	}

	/**
	 * <pre>
	 *     맵 api 가게 아이디로 가게의 밥 모임 조회
	 * </pre>
	 * @param mapStoreId 맵 api 가게 아이디
	 * @param pageable 페이징 데이터
	 * @return status : 200, body : 해당 가게의 현재 모집 중인 밥 모임 데이터
	 */
	@GetMapping(value = "/page/{mapStoreId}", produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponse<CrewPageResponse>> getByMapStoreId
	(
		@PathVariable String mapStoreId,
		@PageableDefault(sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable
	) {

		CrewPageResponse responses = crewService.getByPlaceId(mapStoreId, pageable);
		return ResponseEntity.ok().body(new ApiResponse<>(responses));
	}

	/**
	 * <pre>
	 *     사용자의 위도, 경도로 특정 거리 안에 있는 밥 모임 조회
	 * </pre>
	 * @param distanceRequest 사용자 경도, 위도, 모임을 찾을 반경 거리 DTO
	 * @return status : 200, body : 사용자 위치를 기반으로 특정 거리 안에 있는 밥 모임 데이터
	 */
	@GetMapping(produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponse<CrewResponses>> getByLocation
	(
		@ModelAttribute @Valid SearchCrewRequest distanceRequest
	) {
		CrewResponses responses = crewService.getByLocation(distanceRequest);
		return ResponseEntity.ok().body(new ApiResponse<>(responses));
	}

	/**
	 * <pre>
	 *     밥 모임 상태 변경
	 * </pre>
	 * @param updateStatusRequest 밤 모임 아이디와, 변경할 상태 DTO
	 * @param user 사용자 정보
	 * @return status : 200
	 */
	@PatchMapping
	public ResponseEntity<Void> updateStatus
	(
		@RequestBody @Valid UpdateStatusRequest updateStatusRequest,
		@AuthenticationPrincipal JwtAuthentication user
	) {
		crewService.updateStatus(updateStatusRequest);
		return ResponseEntity.ok().build();
	}

}
