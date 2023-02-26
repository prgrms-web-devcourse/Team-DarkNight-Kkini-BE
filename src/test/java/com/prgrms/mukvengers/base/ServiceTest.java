package com.prgrms.mukvengers.base;

import static com.prgrms.mukvengers.utils.StoreObjectProvider.*;
import static com.prgrms.mukvengers.utils.UserObjectProvider.*;

import org.junit.jupiter.api.BeforeEach;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.prgrms.mukvengers.domain.crew.repository.CrewRepository;
import com.prgrms.mukvengers.domain.crew.service.CrewService;
import com.prgrms.mukvengers.domain.store.model.Store;
import com.prgrms.mukvengers.domain.store.repository.StoreRepository;
import com.prgrms.mukvengers.domain.store.service.StoreService;
import com.prgrms.mukvengers.domain.user.model.User;
import com.prgrms.mukvengers.domain.user.repository.UserRepository;

@SpringBootTest
public abstract class ServiceTest {

	@Autowired
	protected UserRepository userRepository;

	@Autowired
	protected StoreService storeService;

	@Autowired
	protected StoreRepository storeRepository;

	@Autowired
	protected CrewRepository crewRepository;

	@Autowired
	protected CrewService crewService;

	protected GeometryFactory gf = new GeometryFactory();

	protected User savedUser;
	protected Store savedStore;

	@BeforeEach
	void setUpLogin() {
		savedUser = userRepository.save(createUser());

		savedStore = storeRepository.save(createStore());
	}
}
