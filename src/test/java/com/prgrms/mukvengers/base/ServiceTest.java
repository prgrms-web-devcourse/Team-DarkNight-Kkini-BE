package com.prgrms.mukvengers.base;

import static com.prgrms.mukvengers.utils.StoreObjectProvider.*;
import static com.prgrms.mukvengers.utils.UserObjectProvider.*;

import org.junit.jupiter.api.BeforeEach;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.mukvengers.domain.crew.repository.CrewRepository;
import com.prgrms.mukvengers.domain.crew.service.CrewService;
import com.prgrms.mukvengers.domain.crewmember.repository.CrewMemberRepository;
import com.prgrms.mukvengers.domain.proposal.repository.ProposalRepository;
import com.prgrms.mukvengers.domain.proposal.service.ProposalService;
import com.prgrms.mukvengers.domain.review.repository.ReviewRepository;
import com.prgrms.mukvengers.domain.review.service.ReviewService;
import com.prgrms.mukvengers.domain.store.model.Store;
import com.prgrms.mukvengers.domain.store.repository.StoreRepository;
import com.prgrms.mukvengers.domain.store.service.StoreService;
import com.prgrms.mukvengers.domain.user.model.User;
import com.prgrms.mukvengers.domain.user.repository.UserRepository;
import com.prgrms.mukvengers.domain.user.service.UserService;

@SpringBootTest
@Transactional
public abstract class ServiceTest {

	@Autowired
	protected UserService userService;

	@Autowired
	protected UserRepository userRepository;

	@Autowired
	protected StoreService storeService;

	@Autowired
	protected StoreRepository storeRepository;

	@Autowired
	protected CrewService crewService;

	@Autowired
	protected CrewRepository crewRepository;

	@Autowired
	protected CrewMemberRepository crewMemberRepository;

	@Autowired
	protected ReviewService reviewService;

	@Autowired
	protected ProposalService proposalService;

	@Autowired
	protected ProposalRepository proposalRepository;

	@Autowired
	protected ReviewRepository reviewRepository;

	protected GeometryFactory gf = new GeometryFactory();

	protected User savedUser;

	protected Store savedStore;

	protected Long savedUserId;

	@BeforeEach
	void setUpLogin() {

		savedUser = userRepository.save(createUser());

		savedUserId = savedUser.getId();

		savedStore = storeRepository.save(createStore());
	}
}
