package com.prgrms.mukvengers.domain.user.api;

import static org.springframework.http.HttpStatus.*;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.mukvengers.domain.user.dto.request.UpdateUserRequest;
import com.prgrms.mukvengers.domain.user.dto.response.UserProfileResponse;
import com.prgrms.mukvengers.domain.user.service.UserService;
import com.prgrms.mukvengers.global.common.dto.ApiResponse;
import com.prgrms.mukvengers.global.security.jwt.JwtAuthentication;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping("/{userId}")
	public ResponseEntity<ApiResponse<UserProfileResponse>> getUserProfile(
		@PathVariable Long userId
	) {
		return ResponseEntity.ok()
			.body(new ApiResponse<>(userService.getUserProfile(userId)));
	}

	@GetMapping("/me")
	public ResponseEntity<ApiResponse<UserProfileResponse>> getMyProfile(
		@AuthenticationPrincipal JwtAuthentication user
	) {
		return ResponseEntity.ok()
			.body(new ApiResponse<>(userService.getUserProfile(user.id())));
	}

	@PutMapping("/me")
	public ResponseEntity<ApiResponse<UserProfileResponse>> updateMyProfile(
		@RequestBody @Valid UpdateUserRequest updateUserRequest,
		@AuthenticationPrincipal JwtAuthentication user
	) {
		return ResponseEntity.ok()
			.body(new ApiResponse<>(userService.updateUserProfile(updateUserRequest, user.id())));
	}

	@DeleteMapping("/me")
	@ResponseStatus(NO_CONTENT)
	public void deleteMyProfile(
		@AuthenticationPrincipal JwtAuthentication user
	) {
		userService.deleteUser(user.id());
	}

}
