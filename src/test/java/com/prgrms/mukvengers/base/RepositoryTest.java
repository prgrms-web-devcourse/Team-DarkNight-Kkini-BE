package com.prgrms.mukvengers.base;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crew.repository.CrewRepository;
import com.prgrms.mukvengers.domain.store.model.Store;
import com.prgrms.mukvengers.domain.store.repository.StoreRepository;
import com.prgrms.mukvengers.domain.user.model.User;
import com.prgrms.mukvengers.domain.user.repository.UserRepository;
import com.prgrms.mukvengers.global.config.jpa.JpaConfig;
import com.prgrms.mukvengers.utils.CrewObjectProvider;
import com.prgrms.mukvengers.utils.StoreObjectProvider;
import com.prgrms.mukvengers.utils.UserObjectProvider;

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

	protected User savedUser;

	protected Store savedStore;

	protected List<Crew> crews;

	@BeforeEach
	void setUp() {

		savedUser = UserObjectProvider.createUser();
		userRepository.save(savedUser);

		savedStore = StoreObjectProvider.createStore();
		storeRepository.save(savedStore);

		crews = CrewObjectProvider.createCrews(savedUser, savedStore);
		crewRepository.saveAll(crews);

	}
}
