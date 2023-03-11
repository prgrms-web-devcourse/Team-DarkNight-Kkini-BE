package com.prgrms.mukvengers.domain.proposal.api;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.*;
import static com.epages.restdocs.apispec.ResourceDocumentation.*;
import static com.epages.restdocs.apispec.ResourceSnippetParameters.*;
import static com.prgrms.mukvengers.utils.CrewObjectProvider.*;
import static com.prgrms.mukvengers.utils.UserObjectProvider.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.epages.restdocs.apispec.Schema;
import com.epages.restdocs.apispec.SimpleType;
import com.prgrms.mukvengers.base.ControllerTest;
import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crewmember.model.CrewMember;
import com.prgrms.mukvengers.domain.crewmember.model.vo.CrewMemberRole;
import com.prgrms.mukvengers.domain.proposal.dto.request.CreateProposalRequest;
import com.prgrms.mukvengers.domain.proposal.dto.request.UpdateProposalRequest;
import com.prgrms.mukvengers.domain.proposal.model.Proposal;
import com.prgrms.mukvengers.domain.user.model.User;
import com.prgrms.mukvengers.utils.CrewMemberObjectProvider;
import com.prgrms.mukvengers.utils.ProposalObjectProvider;

class ProposalControllerTest extends ControllerTest {

	public static final Schema GET_PROPOSALS_BY_LEADER_ID_PROPOSAL_RESPONSE = new Schema(
		"getProposalsByLeaderIdResponse");
	public static final Schema CREATE_PROPOSAL_REQUEST = new Schema("createProposal");
	public static final Schema UPDATE_PROPOSAL_REQUEST = new Schema("updateProposal");
	public static final Schema FIND_BY_PROPOSAL_ID_REQUEST = new Schema("findByProposalIdCRequest");

	@Test
	@DisplayName("[성공] 사용자는 신청서를 작성할 수 있다.")
	void createProposal_success() throws Exception {

		User leader = userRepository.save(createUser("1232456789"));

		Crew crew = crewRepository.save(createCrew(savedStore));

		CreateProposalRequest proposalRequest = ProposalObjectProvider.createProposalRequest(leader.getId());

		String jsonRequest = objectMapper.writeValueAsString(proposalRequest);

		mockMvc.perform(post("/api/v1/crews/{crewId}/proposals", crew.getId())
				.contentType(APPLICATION_JSON)
				.header(AUTHORIZATION, BEARER_TYPE + accessToken1)
				.content(jsonRequest))
			.andExpect(status().isCreated())
			.andDo(print())
			.andDo(document("proposal-create",
				resource(
					builder()
						.tag(PROPOSAL)
						.summary("신청서 생성 API")
						.description("사용자는 신청서를 작성할 수 있다.")
						.requestSchema(CREATE_PROPOSAL_REQUEST)
						.requestFields(
							fieldWithPath("leaderId").type(NUMBER).description("해당 밥모임의 리더 아이디"),
							fieldWithPath("content").type(STRING).description("신청서 내용")
						)
						.responseHeaders(
							headerWithName("Location").description("조회해볼 수 있는 요청 주소"))
						.build()
				)
			));
	}

	@Test
	@DisplayName("[성공] 신청서 아이디로 신청서를 조회한다.")
	void getById_success() throws Exception {

		User user = createUser("1232456789");
		userRepository.save(user);

		Crew crew = createCrew(savedStore);
		crewRepository.save(crew);

		Proposal proposal = ProposalObjectProvider.createProposal(user, savedUser1.getId(), crew.getId());
		proposalRepository.save(proposal);

		mockMvc.perform(get("/api/v1/proposals/{proposalId}", proposal.getId())
				.header(AUTHORIZATION, BEARER_TYPE + accessToken1)
				.accept(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data").exists())
			.andDo(print())
			.andDo(document("proposal-getById",
				resource(
					builder()
						.tag(PROPOSAL)
						.summary("신청서 아이디로 신청서 조회")
						.description("신청서의 아이디로 신청서를 조회합니다.")
						.responseSchema(FIND_BY_PROPOSAL_ID_REQUEST)
						.pathParameters(
							parameterWithName("proposalId").type(SimpleType.STRING).description("신청서 아이디")
						)
						.responseFields(
							fieldWithPath("data.id").type(NUMBER).description("신청서 아이디"),
							fieldWithPath("data.content").type(STRING).description("신청서 내용"),
							fieldWithPath("data.status").type(STRING).description("신청서 상태"),
							fieldWithPath("data.leaderId").type(NUMBER).description("모임의 방장 아이디"),
							fieldWithPath("data.crewId").type(NUMBER).description("모임 아이디"),
							fieldWithPath("data.user.id").type(NUMBER).description("유저 ID"),
							fieldWithPath("data.user.nickname").type(STRING).description("닉네임"),
							fieldWithPath("data.user.profileImgUrl").type(STRING).description("프로필 이미지"),
							fieldWithPath("data.user.introduction").type(STRING).description("한줄 소개"),
							fieldWithPath("data.user.leaderCount").type(NUMBER).description("방장 횟수"),
							fieldWithPath("data.user.crewCount").type(NUMBER).description("모임 참여 횟수"),
							fieldWithPath("data.user.tasteScore").type(NUMBER).description("맛잘알 점수"),
							fieldWithPath("data.user.mannerScore").type(NUMBER).description("매너 온도"),
							fieldWithPath("data.user.mannerScore").type(NUMBER).description("매너 온도"))
						.build()
				)
			));

	}

	@Test
	@DisplayName("[성공] 사용자가 방장인 모임의 모든 신청서를 조회한다.")
	void getProposalsByLeaderId_success() throws Exception {

		User user = createUser("1232456789");
		userRepository.save(user);

		Crew crew = createCrew(savedStore);
		crewRepository.save(crew);

		List<Proposal> proposals = ProposalObjectProvider.createProposals(user, savedUser1.getId(), crew.getId());
		proposalRepository.saveAll(proposals);

		mockMvc.perform(get("/api/v1/proposals/leader")
				.header(AUTHORIZATION, BEARER_TYPE + accessToken1)
				.accept(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data").exists())
			.andDo(print())
			.andDo(document("proposal-getProposalsByLeaderId",
				resource(
					builder()
						.tag(PROPOSAL)
						.summary("사용자가 방장인 모임의 모든 신청서 조회")
						.description("사용자가 방장인 모임에서 모임 신청을 위해 작서된 신청서를 모두 조회를 위한 API 입니다.")
						.responseSchema(GET_PROPOSALS_BY_LEADER_ID_PROPOSAL_RESPONSE)
						.responseFields(
							fieldWithPath("data.responses.[].user.id").type(NUMBER).description("유저 ID"),
							fieldWithPath("data.responses.[].user.nickname").type(STRING).description("닉네임"),
							fieldWithPath("data.responses.[].user.profileImgUrl").type(STRING).description("프로필 이미지"),
							fieldWithPath("data.responses.[].user.introduction").type(STRING).description("한줄 소개"),
							fieldWithPath("data.responses.[].user.leaderCount").type(NUMBER).description("방장 횟수"),
							fieldWithPath("data.responses.[].user.crewCount").type(NUMBER).description("모임 참여 횟수"),
							fieldWithPath("data.responses.[].user.tasteScore").type(NUMBER).description("맛잘알 점수"),
							fieldWithPath("data.responses.[].user.mannerScore").type(NUMBER).description("매너 온도"),
							fieldWithPath("data.responses.[].id").type(NUMBER).description("신청서 아이디"),
							fieldWithPath("data.responses.[].content").type(STRING).description("신청서 내용"),
							fieldWithPath("data.responses.[].status").type(STRING).description("신청서 상태"),
							fieldWithPath("data.responses.[].leaderId").type(NUMBER).description("모임의 방장 아이디"),
							fieldWithPath("data.responses.[].crewId").type(NUMBER).description("모임 아이디")
						)
						.build()
				)
			));

	}

	@Test
	@DisplayName("[성공] 방장이 신청서를 승인하는 경우 신청서의 상태값이 'APPROVE' 로 변경되며 밥모임원에 저장된다.")
	void update_proposalStatus_approve_success() throws Exception {

		Crew creatCrew = createCrew(savedStore);
		Crew crew = crewRepository.save(creatCrew);

		CrewMember crewMemberOfLeader = CrewMemberObjectProvider.createCrewMember(savedUser1Id, crew,
			CrewMemberRole.LEADER);
		CrewMember leader = crewMemberRepository.save(crewMemberOfLeader);

		User createUser = createUser("1232456789");
		User user = userRepository.save(createUser);

		CrewMember crewMemberOfMember = CrewMemberObjectProvider.createCrewMember(user.getId(), crew,
			CrewMemberRole.MEMBER);
		crewMemberRepository.save(crewMemberOfMember);

		Proposal createProposal = ProposalObjectProvider.createProposal(user, leader.getId(), crew.getId());
		Proposal proposal = proposalRepository.save(createProposal);

		UpdateProposalRequest proposalRequest = new UpdateProposalRequest("승인");

		String jsonRequest = objectMapper.writeValueAsString(proposalRequest);

		mockMvc.perform(patch("/api/v1/proposals/{proposalId}", proposal.getId())
				.contentType(APPLICATION_JSON)
				.header(AUTHORIZATION, BEARER_TYPE + accessToken1)
				.content(jsonRequest))
			.andExpect(status().isOk())
			.andDo(document("proposal-Approve",
				resource(
					builder()
						.tag(PROPOSAL)
						.summary("신청서 상태 승인 API")
						.description("방장으로부터 신청서가 승인되면 밥모임원에 등록된다.")
						.requestSchema(UPDATE_PROPOSAL_REQUEST)
						.requestFields(
							fieldWithPath("proposalStatus").type(STRING).description("신청서 응답 상태")
						)
						.responseFields()
						.build()
				)
			));

	}

	@Test
	@DisplayName("[성공] 방장이 신청서를 거절하는 경우 신청서의 상태값이 'REFUSE' 로 변경되며 밥모임원에 저장된다.")
	void update_proposalStatus_refuse_success() throws Exception {

		User createUser = createUser("1232456789");
		User user = userRepository.save(createUser);

		Crew creatCrew = createCrew(savedStore);
		Crew crew = crewRepository.save(creatCrew);

		CrewMember crewMemberOfLeader = CrewMemberObjectProvider.createCrewMember(user.getId(), crew,
			CrewMemberRole.LEADER);
		CrewMember leader = crewMemberRepository.save(crewMemberOfLeader);

		CrewMember crewMemberOfMember = CrewMemberObjectProvider.createCrewMember(user.getId(), crew,
			CrewMemberRole.MEMBER);
		crewMemberRepository.save(crewMemberOfMember);

		Proposal createProposal = ProposalObjectProvider.createProposal(user, leader.getId(), crew.getId());
		Proposal proposal = proposalRepository.save(createProposal);

		UpdateProposalRequest proposalRequest = new UpdateProposalRequest("거절");

		String jsonRequest = objectMapper.writeValueAsString(proposalRequest);

		mockMvc.perform(patch("/api/v1/proposals/{proposalId}", proposal.getId())
				.contentType(APPLICATION_JSON)
				.header(AUTHORIZATION, BEARER_TYPE + accessToken1)
				.content(jsonRequest))
			.andExpect(status().isOk())
			.andDo(document("proposal-Refuse",
				resource(
					builder()
						.tag(PROPOSAL)
						.summary("신청서 상태 거절 API")
						.description("방장으로부터 신청서가 거절되면 밥모임원에 저장되지 않는다.")
						.requestSchema(UPDATE_PROPOSAL_REQUEST)
						.requestFields(
							fieldWithPath("proposalStatus").type(STRING).description("신청서 응답 상태")
						)
						.responseFields()
						.build()
				)
			));

	}

	@Test
	@DisplayName("[성공] 사용자가 방장인 아니고 참여자인 모임의 신청서를 모두 조회합니다.")
	void getProposalsByMemberId_success() throws Exception {

		User user = createUser("1232456789");
		userRepository.save(user);

		Crew crew = createCrew(savedStore);
		crewRepository.save(crew);

		List<Proposal> proposals = ProposalObjectProvider.createProposals(savedUser1, user.getId(), crew.getId());
		proposalRepository.saveAll(proposals);

		mockMvc.perform(get("/api/v1/proposals/member")
				.header(AUTHORIZATION, BEARER_TYPE + accessToken1)
				.accept(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data").exists())
			.andDo(print())
			.andDo(document("proposal-getProposalsByLeaderId",
				resource(
					builder()
						.tag(PROPOSAL)
						.summary("사용자가 방장인 모임의 모든 신청서 조회")
						.description("사용자가 방장인 모임에서 모임 신청을 위해 작서된 신청서를 모두 조회를 위한 API 입니다.")
						.responseSchema(GET_PROPOSALS_BY_LEADER_ID_PROPOSAL_RESPONSE)
						.responseFields(
							fieldWithPath("data.responses.[].user.id").type(NUMBER).description("유저 ID"),
							fieldWithPath("data.responses.[].user.nickname").type(STRING).description("닉네임"),
							fieldWithPath("data.responses.[].user.profileImgUrl").type(STRING).description("프로필 이미지"),
							fieldWithPath("data.responses.[].user.introduction").type(STRING).description("한줄 소개"),
							fieldWithPath("data.responses.[].user.leaderCount").type(NUMBER).description("방장 횟수"),
							fieldWithPath("data.responses.[].user.crewCount").type(NUMBER).description("모임 참여 횟수"),
							fieldWithPath("data.responses.[].user.tasteScore").type(NUMBER).description("맛잘알 점수"),
							fieldWithPath("data.responses.[].user.mannerScore").type(NUMBER).description("매너 온도"),
							fieldWithPath("data.responses.[].user.mannerScore").type(NUMBER).description("매너 온도"),
							fieldWithPath("data.responses.[].id").type(NUMBER).description("신청서 아이디"),
							fieldWithPath("data.responses.[].content").type(STRING).description("신청서 내용"),
							fieldWithPath("data.responses.[].status").type(STRING).description("신청서 상태"),
							fieldWithPath("data.responses.[].leaderId").type(NUMBER).description("모임의 방장 아이디"),
							fieldWithPath("data.responses.[].crewId").type(NUMBER).description("모임 아이디")
						)
						.build()
				)
			));

	}
}