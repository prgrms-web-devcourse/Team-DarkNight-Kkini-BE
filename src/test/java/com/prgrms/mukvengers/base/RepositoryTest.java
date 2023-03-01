package com.prgrms.mukvengers.base;

import static com.prgrms.mukvengers.utils.CrewObjectProvider.*;
import static com.prgrms.mukvengers.utils.StoreObjectProvider.*;
import static com.prgrms.mukvengers.utils.UserObjectProvider.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crew.repository.CrewRepository;
import com.prgrms.mukvengers.domain.crewmember.repository.CrewMemberRepository;
import com.prgrms.mukvengers.domain.review.repository.ReviewRepository;
import com.prgrms.mukvengers.domain.store.model.Store;
import com.prgrms.mukvengers.domain.store.repository.StoreRepository;
import com.prgrms.mukvengers.domain.user.model.User;
import com.prgrms.mukvengers.domain.user.repository.UserRepository;
import com.prgrms.mukvengers.global.config.jpa.JpaConfig;

@DataJpaTest
@Import(JpaConfig.class)
@AutoConfigureTestDatabase(replace = NONE)
public abstract class RepositoryTest {

	@Autowired
	protected UserRepository userRepository;

	@Autowired
	protected StoreRepository storeRepository;

	@Autowired
	protected CrewRepository crewRepository;

	@Autowired
	protected CrewMemberRepository crewMemberRepository;

	@Autowired
	protected ReviewRepository reviewRepository;

	protected User savedUser;

	protected Store savedStore;

	protected List<Crew> crews;

	protected Long savedUserId;

	@BeforeEach
	void setUp() {

		savedUser = userRepository.save(createUser());

		savedUserId = savedUser.getId();

		savedStore = storeRepository.save(createStore());

		crews = createCrews(savedStore);

		crewRepository.saveAll(crews); // 위험할 가능성이 높음
	}
}
