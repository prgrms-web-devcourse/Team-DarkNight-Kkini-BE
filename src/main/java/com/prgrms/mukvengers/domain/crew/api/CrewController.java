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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.prgrms.mukvengers.domain.crew.dto.request.CreateCrewRequest;
import com.prgrms.mukvengers.domain.crew.dto.request.UpdateStatusRequest;
import com.prgrms.mukvengers.domain.crew.dto.response.CrewPageResponse;
import com.prgrms.mukvengers.domain.crew.dto.response.CrewResponses;
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

	/**
	 * <pre>
	 *     밥 모임 생성
	 * </pre>
	 * @param createCrewRequest 밥 모임 정보 DTO
	 * @param user 유저 정보
	 * @return status : 201, body : 생성된 밥 모임 조회 redirectUri
	 */
	@PostMapping(consumes = APPLICATION_JSON_VALUE)
	public ResponseEntity<URI> create(
		@RequestBody @Valid CreateCrewRequest createCrewRequest,
		@AuthenticationPrincipal JwtAuthentication user) {

		IdResponse idResponse = crewService.create(createCrewRequest, user.id());

		String createURL = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString() + "/" + idResponse.id();

		return ResponseEntity.created(URI.create(createURL)).build();

	}

	/**
	 * <pre>
	 *     맵 api 가게 아이디로 가게의 밥 모임 조회
	 * </pre>
	 * @param mapStoreId 맵 api 가게 아이디
	 * @param pageable 페이지 정보
	 * @return status : 200, body : 해당 가게의 현재 모집 중인 밥 모임 정보
	 */
	@GetMapping(value = "/{mapStoreId}", produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponse<CrewPageResponse>> findByMapStoreId(
		@PathVariable String mapStoreId,
		@PageableDefault(sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {

		CrewPageResponse responses = crewService.getByMapStoreId(mapStoreId, pageable);

		return ResponseEntity.ok().body(new ApiResponse<>(responses));

	}

	/**
	 * <pre>
	 *     사용자의 위도, 경도로 특정 거리 안에 있는 밥 모임 조회
	 * </pre>
	 * @param latitude 사용자 위도
	 * @param longitude 사용자 경도
	 * @return status : 200, body : 사용자 위치를 기반으로 특정 거리 안에 있는 밥 모임 정보
	 */
	@GetMapping(produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponse<CrewResponses>> findByLocation(
		@RequestParam("latitude") String latitude,
		@RequestParam("longitude") String longitude
	) {
		CrewResponses responses = crewService.getByLocation(latitude, longitude);

		return ResponseEntity.ok().body(new ApiResponse<>(responses));
	}

	/**
	 * <pre>
	 *     밥 모임 상태 변경
	 * </pre>
	 * @param updateStatusRequest 밤 모임 아이디와, 변경할 상태 정보
	 * @param user 사용자 정보
	 * @return status : 200
	 */
	@PatchMapping
	public ResponseEntity<Void> updateStatus(
		@RequestBody @Valid UpdateStatusRequest updateStatusRequest,
		@AuthenticationPrincipal JwtAuthentication user
	) {
		crewService.updateStatus(updateStatusRequest);

		return ResponseEntity.ok().build();
	}

}
