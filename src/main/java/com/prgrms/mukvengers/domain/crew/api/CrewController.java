package com.prgrms.mukvengers.domain.crew.api;

import static org.springframework.http.MediaType.*;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.prgrms.mukvengers.domain.crew.dto.request.CreateCrewRequest;
import com.prgrms.mukvengers.domain.crew.dto.response.CrewResponses;
import com.prgrms.mukvengers.domain.crew.service.CrewService;
import com.prgrms.mukvengers.global.common.dto.IdResponse;
import com.prgrms.mukvengers.global.security.jwt.JwtAuthentication;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/crews")
public class CrewController {

	private final CrewService crewService;

	@PostMapping(consumes = APPLICATION_JSON_VALUE)
	public ResponseEntity<URI> create(
		@RequestBody @Valid CreateCrewRequest createCrewRequest,
		@AuthenticationPrincipal JwtAuthentication user) {

		IdResponse idResponse = crewService.create(createCrewRequest, user.id());

		String createURL = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString() + "/" + idResponse.id();

		return ResponseEntity.created(URI.create(createURL)).build();

	}

	@GetMapping(value = "/{mapStoreId}", produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<CrewResponses> findByMapStoreId(
		@PathVariable String mapStoreId) {

		CrewResponses responses = crewService.findByMapStoreId(mapStoreId);

		return ResponseEntity.ok(responses);

	}

	@GetMapping(produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<CrewResponses> findByLocation(
		@RequestParam("x") String x,
		@RequestParam("y") String y
	) {
		CrewResponses responses = crewService.findByLocation(x, y);

		return ResponseEntity.ok(responses);
	}

}
