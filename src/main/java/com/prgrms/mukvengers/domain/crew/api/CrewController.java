package com.prgrms.mukvengers.domain.crew.api;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.prgrms.mukvengers.domain.crew.dto.CreateCrewRequest;
import com.prgrms.mukvengers.domain.crew.service.CrewServiceImpl;
import com.prgrms.mukvengers.global.common.dto.IdResponse;
import com.prgrms.mukvengers.global.security.jwt.JwtAuthentication;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/crew")
public class CrewController {

	private final CrewServiceImpl crewService;

	@PostMapping
	public ResponseEntity<URI> create(
		@RequestBody @Valid CreateCrewRequest createCrewRequest,
		@AuthenticationPrincipal JwtAuthentication user ) {

		IdResponse idResponse = crewService.create(createCrewRequest, user.id());

		String createURL = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString() + "/" + idResponse.id();

		return ResponseEntity.created(URI.create(createURL)).build();


	}
}
