package com.prgrms.mukvengers.domain.store.api;

import static org.springframework.http.MediaType.*;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.prgrms.mukvengers.domain.store.dto.request.CreateStoreRequest;
import com.prgrms.mukvengers.domain.store.dto.response.StoreResponse;
import com.prgrms.mukvengers.domain.store.service.StoreService;
import com.prgrms.mukvengers.global.common.dto.ApiResponse;
import com.prgrms.mukvengers.global.common.dto.IdResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stores")
public class StoreController {

	private final StoreService storeService;

	/**
	 * <pre>
	 *     가게 생성
	 * </pre>
	 * @param createStoreRequest 가게 생성 DTO
	 * @return status : 201, body : 생성된 가게 조회 redirectUri
	 */
	@PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<IdResponse> create(
		@RequestBody @Valid CreateStoreRequest createStoreRequest
	) {
		String placeId = storeService.create(createStoreRequest);
		URI location = UriComponentsBuilder.fromUriString("/api/v1/stores/" + placeId).build().toUri();
		return ResponseEntity.created(location).build();
	}

	/**
	 * <pre>
	 *     맵 api 아이디를 사용하여 단건 조회
	 * </pre>
	 * @param placeId 구글 맵 api 아이디
	 * @return status : 200, body : 조회된 가게 데이터
	 */
	@GetMapping(value = "{placeId}", produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponse<StoreResponse>> getByPlaceId(
		@PathVariable String placeId
	) {
		StoreResponse response = storeService.getByPlaceId(placeId);
		return ResponseEntity.ok(new ApiResponse<>(response));
	}
}
