package com.prgrms.mukvengers.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import com.prgrms.mukvengers.domain.crew.repository.CrewRepository;
import com.prgrms.mukvengers.domain.store.repository.StoreRepository;
import com.prgrms.mukvengers.domain.user.repository.UserRepository;
import com.prgrms.mukvengers.global.infra.data.DataLoader;

@Disabled
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TestDataGenerator {

	@Autowired
	private DataLoader dataLoader;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private StoreRepository storeRepository;

	@Autowired
	private CrewRepository crewRepository;

	private long start;
	private long end;

	@BeforeEach
	void setStart() {
		start = System.currentTimeMillis();
	}

	@AfterEach
	void setEnd() {
		end = System.currentTimeMillis();
		System.out.println("걸린시간 : " + (end - start) / 1000.0 + "초");
	}

	@Test
	void insertUserTest() {
		dataLoader.loadUser(10000);
	}

	@Test
	void insertStoreTest() {
		dataLoader.loadStore(1000);
	}

	@Test
	void insertCrewTest() {// 가게 저장 로직도 포함되어 있음!
		dataLoader.loadCrew(1000, storeRepository);
	}

	@Test
	void insertDefaultSetting() {
		dataLoader.loadSetCrewAndPeople(100, storeRepository, userRepository, crewRepository);
	}

}
