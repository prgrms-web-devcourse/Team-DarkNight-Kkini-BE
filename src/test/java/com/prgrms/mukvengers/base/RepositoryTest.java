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

import com.prgrms.mukvengers.domain.chat.repository.ChatRepository;
import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crew.repository.CrewRepository;
import com.prgrms.mukvengers.domain.crewmember.repository.CrewMemberRepository;
import com.prgrms.mukvengers.domain.proposal.repository.ProposalRepository;
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

	@Autowired
	protected ChatRepository chatRepository;

	@Autowired
	protected ProposalRepository proposalRepository;

	protected User savedUser1;

	protected User savedUser2;

	protected Store savedStore;

	protected List<Crew> crews;

	protected Long savedUser1Id;

	protected Long savedUser2Id;

	@BeforeEach
	void setUp() {

		savedUser1 = userRepository.save(createUser("123"));

		savedUser2 = userRepository.save(createUser("456"));

		savedUser1Id = savedUser1.getId();

		savedUser2Id = savedUser2.getId();

		savedStore = storeRepository.save(createStore());

		crews = createCrews(savedStore);

		crewRepository.saveAll(crews); // 위험할 가능성이 높음
	}
}
