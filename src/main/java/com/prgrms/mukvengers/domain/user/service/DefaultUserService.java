package com.prgrms.mukvengers.domain.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.mukvengers.domain.user.dto.request.UpdateUserRequest;
import com.prgrms.mukvengers.domain.user.dto.response.UserProfileResponse;
import com.prgrms.mukvengers.domain.user.exception.UserNotFoundException;
import com.prgrms.mukvengers.domain.user.mapper.UserMapper;
import com.prgrms.mukvengers.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultUserService implements UserService {

	private final UserMapper userMapper;
	private final UserRepository userRepository;

	/* [회원 조회] 사용자 ID를 통해 등록된 유저 정보 찾아서 제공하고 없으면 예외가 발생합니다. */
	@Override
	public UserProfileResponse getUserProfile(Long userId) {
		return userRepository.findById(userId)
			.map(userMapper::toSingleUserResponse)
			.orElseThrow(() -> new UserNotFoundException(userId));
	}

	/* [회원 프로필 수정] UpdateUserRequest DTO를 사용해서 사용자의 프로필(닉네임, 프로필 이미지, 자기소개)를 한번에 수정합니다. */
	@Override
	@Transactional
	public UserProfileResponse updateUserProfile(UpdateUserRequest updateUserRequest, Long userId) {
		return userRepository.findById(userId)
			.map(user -> user.changeProfile(updateUserRequest))
			.map(userMapper::toSingleUserResponse)
			.orElseThrow(() -> new UserNotFoundException(userId));
	}

	/* [회원 탈퇴] 계정을 삭제합니다. soft delete가 적용됩니다.*/
	@Override
	@Transactional
	public void deleteUser(Long userId) {
		userRepository.findById(userId)
			.ifPresentOrElse(userRepository::delete,
				() -> {
					throw new UserNotFoundException(userId);
				});
	}

}
