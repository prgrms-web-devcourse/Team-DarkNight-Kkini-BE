package com.prgrms.mukvengers.domain.user.repository;

import static com.prgrms.mukvengers.utils.UserObjectProvider.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.EmptyResultDataAccessException;

import com.prgrms.mukvengers.base.RepositoryTest;
import com.prgrms.mukvengers.domain.user.model.User;

class UserRepositoryTest extends RepositoryTest {

	@Test
	@DisplayName("[성공] 유저 엔티티를 저장할 수 있다.")
	void save_success() {
		// given & when
		User savedUser = userRepository.save(createUser());
		Optional<User> foundUser = userRepository.findById(savedUser.getId());
		// then
		assertThat(foundUser).isPresent();
		assertThat(foundUser.get())
			.usingRecursiveComparison()
			.isEqualTo(savedUser);
	}

	@Test
	@DisplayName("[성공] 유저ID로 저장된 유저를 가져올 수 있다.")
	void findById_success() {
		// given
		User savedUser = userRepository.save(createUser());
		// when
		Optional<User> foundUser = userRepository.findById(savedUser.getId());
		// then
		assertThat(foundUser).isPresent();
		assertThat(foundUser.get())
			.usingRecursiveComparison()
			.isEqualTo(savedUser);
	}

	@Test
	@DisplayName("[실패] 저장되지 않은 유저는 조회할 수 없다.")
	void findById_fail() {
		// given
		Long UNSAVED_USER_ID = 0L;
		// when
		Optional<User> foundUser = userRepository.findById(UNSAVED_USER_ID);
		// then
		assertThat(foundUser).isNotPresent();
	}

	@Test
	@DisplayName("[쿼리 확인 필요] 유저를 update Query를 통해서 논리적으로 삭제할 수 있다.(soft delete 적용)")
	void deleteById_success() {
		// given
		User savedUser = userRepository.save(createUser());
		// when & then
		assertDoesNotThrow(
			() -> userRepository.delete(savedUser)
		);
	}

	@Test
	@DisplayName("[실패] 유효하지 않은 유저 ID로는 유저를 삭제할 수 없다.")
	void deleteById_fail() {
		// given
		Long UNSAVED_USER_ID = 0L;
		// when & then
		assertThatThrownBy(() -> userRepository.deleteById(UNSAVED_USER_ID))
			.isInstanceOf(EmptyResultDataAccessException.class);
	}
}
