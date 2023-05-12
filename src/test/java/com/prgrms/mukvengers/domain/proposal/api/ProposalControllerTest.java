package com.prgrms.mukvengers.domain.proposal.api;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.*;
import static com.epages.restdocs.apispec.ResourceDocumentation.*;
import static com.epages.restdocs.apispec.ResourceSnippetParameters.*;
import static com.prgrms.mukvengers.utils.CrewObjectProvider.*;
import static com.prgrms.mukvengers.utils.ProposalObjectProvider.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.epages.restdocs.apispec.Schema;
import com.epages.restdocs.apispec.SimpleType;
import com.prgrms.mukvengers.base.ControllerTest;
import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.proposal.dto.request.CreateProposalRequest;
import com.prgrms.mukvengers.domain.proposal.dto.request.UpdateProposalRequest;
import com.prgrms.mukvengers.domain.proposal.model.Proposal;

class ProposalControllerTest extends ControllerTest {

	public static final Schema GET_PROPOSALS_BY_LEADER_ID_PROPOSAL_RESPONSE = new Schema(
		"getProposalsByLeaderIdResponse");
	public static final Schema CREATE_PROPOSAL_REQUEST = new Schema("createProposal");
	public static final Schema UPDATE_PROPOSAL_REQUEST = new Schema("updateProposal");
	public static final Schema FIND_BY_PROPOSAL_ID_REQUEST = new Schema("findByProposalIdCRequest");

	private Long proposalId;
	private Long crewId;

	@BeforeEach
	void setUp() {

		Crew crew = crewRepository.save(createCrew(savedStore));
		crewId = crew.getId();

		List<Proposal> proposals = createProposals(savedUser2, savedUser1.getId(), crew.getId());
		proposalRepository.saveAll(proposals);
		proposalId = proposals.get(0).getId();

	}

	@Test
	@DisplayName("[성공] 사용자는 신청서를 작성할 수 있다.")
	void createProposal_success() throws Exception {

		CreateProposalRequest proposalRequest = createProposalRequest(savedUser2Id);

		String jsonRequest = objectMapper.writeValueAsString(proposalRequest);

		mockMvc.perform(post("/api/v1/crews/{crewId}/proposals", crewId)
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

		mockMvc.perform(get("/api/v1/proposals/{proposalId}", proposalId)
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
							fieldWithPath("data.storeName").type(STRING).description("가게 이름"),
							fieldWithPath("data.crewName").type(STRING).description("모임 이름"),
							fieldWithPath("data.user.id").type(NUMBER).description("유저 ID"),
							fieldWithPath("data.user.nickname").type(STRING).description("닉네임"),
							fieldWithPath("data.user.profileImgUrl").type(STRING).description("프로필 이미지"),
							fieldWithPath("data.user.introduction").type(STRING).description("한줄 소개"),
							fieldWithPath("data.user.leaderCount").type(NUMBER).description("방장 횟수"),
							fieldWithPath("data.user.crewCount").type(NUMBER).description("모임 참여 횟수"),
							fieldWithPath("data.user.tasteScore").type(NUMBER).description("맛잘알 점수"),
							fieldWithPath("data.user.mannerScore").type(STRING).description("매너 온도"))
						.build()
				)
			));

	}

	@Test
	@DisplayName("[성공] 사용자가 방장인 모임의 모든 신청서를 조회한다.")
	void getProposalsByLeaderId_success() throws Exception {

		Integer page = 0;

		Integer size = 5;

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("page", String.valueOf(page));
		params.add("size", String.valueOf(size));

		mockMvc.perform(get("/api/v1/proposals/leader")
				.params(params)
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
							fieldWithPath("data.responses.content.[].user.id").type(NUMBER).description("유저 ID"),
							fieldWithPath("data.responses.content.[].user.nickname").type(STRING).description("닉네임"),
							fieldWithPath("data.responses.content.[].user.profileImgUrl").type(STRING)
								.description("프로필 이미지"),
							fieldWithPath("data.responses.content.[].user.introduction").type(STRING)
								.description("한줄 소개"),
							fieldWithPath("data.responses.content.[].user.leaderCount").type(NUMBER)
								.description("방장 횟수"),
							fieldWithPath("data.responses.content.[].user.crewCount").type(NUMBER)
								.description("모임 참여 횟수"),
							fieldWithPath("data.responses.content.[].user.tasteScore").type(NUMBER)
								.description("맛잘알 점수"),
							fieldWithPath("data.responses.content.[].user.mannerScore").type(STRING)
								.description("매너 온도"),
							fieldWithPath("data.responses.content.[].id").type(NUMBER).description("신청서 아이디"),
							fieldWithPath("data.responses.content.[].content").type(STRING).description("신청서 내용"),
							fieldWithPath("data.responses.content.[].status").type(STRING).description("신청서 상태"),
							fieldWithPath("data.responses.content.[].leaderId").type(NUMBER).description("모임의 방장 아이디"),
							fieldWithPath("data.responses.content.[].storeName").type(STRING).description("가게 이름"),
							fieldWithPath("data.responses.content.[].crewName").type(STRING).description("모임 이름"),
							fieldWithPath("data.responses.content.[].crewId").type(NUMBER).description("모임 아이디"),
							fieldWithPath("data.responses.pageable.sort.empty").type(BOOLEAN).description("빈 페이지 여부"),
							fieldWithPath("data.responses.pageable.sort.sorted").type(BOOLEAN).description("페이지 정렬 여부"),
							fieldWithPath("data.responses.pageable.sort.unsorted").type(BOOLEAN)
								.description("페이지 비정렬 여부"),
							fieldWithPath("data.responses.pageable.offset").type(NUMBER).description("페이지 오프셋"),
							fieldWithPath("data.responses.pageable.pageNumber").type(NUMBER).description("페이지 번호"),
							fieldWithPath("data.responses.pageable.pageSize").type(NUMBER)
								.description("한 페이지에 나타내는 원소 수"),
							fieldWithPath("data.responses.pageable.paged").type(BOOLEAN).description("페이지 정보 포함 여부"),
							fieldWithPath("data.responses.pageable.unpaged").type(BOOLEAN).description("페이지 정보 비포함 여부"),
							fieldWithPath("data.responses.last").type(BOOLEAN).description("마지막 페이지 여부"),
							fieldWithPath("data.responses.size").type(NUMBER).description("페이지 사이즈"),
							fieldWithPath("data.responses.number").type(NUMBER).description("페이지 번호"),
							fieldWithPath("data.responses.sort.empty").type(BOOLEAN).description("빈 페이지 여부"),
							fieldWithPath("data.responses.sort.sorted").type(BOOLEAN).description("페이지 정렬 여부"),
							fieldWithPath("data.responses.sort.unsorted").type(BOOLEAN).description("페이지 비정렬 여부"),
							fieldWithPath("data.responses.first").type(BOOLEAN).description("첫 번째 페이지 여부"),
							fieldWithPath("data.responses.numberOfElements").type(NUMBER).description("페이지 원소 개수"),
							fieldWithPath("data.responses.empty").type(BOOLEAN).description("빈 페이지 여부"),
							fieldWithPath("data.responses.totalPages").type(NUMBER).description("전체 페이지 개수"),
							fieldWithPath("data.responses.totalElements").type(NUMBER).description("전체 데이터 개수"))
						.build()
				)
			));

	}

	@Test // 신청서가 거절 됐는데 왜 저장이 되는건가요?
	@DisplayName("[성공] 방장이 신청서를 거절하는 경우 신청서의 상태값이 'REFUSE' 로 변경되며 밥모임원에 저장된다.")
	void update_proposalStatus_refuse_success() throws Exception {

		UpdateProposalRequest proposalRequest = new UpdateProposalRequest("거절");

		String jsonRequest = objectMapper.writeValueAsString(proposalRequest);

		mockMvc.perform(patch("/api/v1/proposals/{proposalId}", proposalId)
				.contentType(APPLICATION_JSON)
				.header(AUTHORIZATION, BEARER_TYPE + accessToken1)
				.content(jsonRequest))
			.andExpect(status().isNoContent())
			.andDo(document("proposal-Refuse",
				resource(
					builder()
						.tag(PROPOSAL)
						.summary("신청서 상태 변경 API")
						.description("방장은 신청서의 상태를 변경할 수 있다.")
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

		Integer page = 0;

		Integer size = 5;

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("page", String.valueOf(page));
		params.add("size", String.valueOf(size));

		mockMvc.perform(get("/api/v1/proposals/member")
				.params(params)
				.header(AUTHORIZATION, BEARER_TYPE + accessToken2)
				.accept(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data").exists())
			.andDo(print())
			.andDo(document("proposal-getProposalsBymemberId",
				resource(
					builder()
						.tag(PROPOSAL)
						.summary("사용자가 참여자인 모임의 모든 신청서 조회")
						.description("사용자가 방장인 모임에서 모임 신청을 위해 작서된 신청서를 모두 조회를 위한 API 입니다.")
						.responseSchema(GET_PROPOSALS_BY_LEADER_ID_PROPOSAL_RESPONSE)
						.responseFields(
							fieldWithPath("data.responses.content.[].user.id").type(NUMBER).description("유저 ID"),
							fieldWithPath("data.responses.content.[].user.nickname").type(STRING).description("닉네임"),
							fieldWithPath("data.responses.content.[].user.profileImgUrl").type(STRING)
								.description("프로필 이미지"),
							fieldWithPath("data.responses.content.[].user.introduction").type(STRING)
								.description("한줄 소개"),
							fieldWithPath("data.responses.content.[].user.leaderCount").type(NUMBER)
								.description("방장 횟수"),
							fieldWithPath("data.responses.content.[].user.crewCount").type(NUMBER)
								.description("모임 참여 횟수"),
							fieldWithPath("data.responses.content.[].user.tasteScore").type(NUMBER)
								.description("맛잘알 점수"),
							fieldWithPath("data.responses.content.[].user.mannerScore").type(STRING)
								.description("매너 온도"),
							fieldWithPath("data.responses.content.[].id").type(NUMBER).description("신청서 아이디"),
							fieldWithPath("data.responses.content.[].content").type(STRING).description("신청서 내용"),
							fieldWithPath("data.responses.content.[].status").type(STRING).description("신청서 상태"),
							fieldWithPath("data.responses.content.[].leaderId").type(NUMBER).description("모임의 방장 아이디"),
							fieldWithPath("data.responses.content.[].storeName").type(STRING).description("가게 이름"),
							fieldWithPath("data.responses.content.[].crewName").type(STRING).description("모임 이름"),
							fieldWithPath("data.responses.content.[].crewId").type(NUMBER).description("모임 아이디"),
							fieldWithPath("data.responses.pageable.sort.empty").type(BOOLEAN).description("빈 페이지 여부"),
							fieldWithPath("data.responses.pageable.sort.sorted").type(BOOLEAN).description("페이지 정렬 여부"),
							fieldWithPath("data.responses.pageable.sort.unsorted").type(BOOLEAN)
								.description("페이지 비정렬 여부"),
							fieldWithPath("data.responses.pageable.offset").type(NUMBER).description("페이지 오프셋"),
							fieldWithPath("data.responses.pageable.pageNumber").type(NUMBER).description("페이지 번호"),
							fieldWithPath("data.responses.pageable.pageSize").type(NUMBER)
								.description("한 페이지에 나타내는 원소 수"),
							fieldWithPath("data.responses.pageable.paged").type(BOOLEAN).description("페이지 정보 포함 여부"),
							fieldWithPath("data.responses.pageable.unpaged").type(BOOLEAN).description("페이지 정보 비포함 여부"),
							fieldWithPath("data.responses.last").type(BOOLEAN).description("마지막 페이지 여부"),
							fieldWithPath("data.responses.size").type(NUMBER).description("페이지 사이즈"),
							fieldWithPath("data.responses.number").type(NUMBER).description("페이지 번호"),
							fieldWithPath("data.responses.sort.empty").type(BOOLEAN).description("빈 페이지 여부"),
							fieldWithPath("data.responses.sort.sorted").type(BOOLEAN).description("페이지 정렬 여부"),
							fieldWithPath("data.responses.sort.unsorted").type(BOOLEAN).description("페이지 비정렬 여부"),
							fieldWithPath("data.responses.first").type(BOOLEAN).description("첫 번째 페이지 여부"),
							fieldWithPath("data.responses.numberOfElements").type(NUMBER).description("페이지 원소 개수"),
							fieldWithPath("data.responses.empty").type(BOOLEAN).description("빈 페이지 여부"),
							fieldWithPath("data.responses.totalPages").type(NUMBER).description("전체 페이지 개수"),
							fieldWithPath("data.responses.totalElements").type(NUMBER).description("전체 데이터 개수"))
						.build()
				)
			));

	}

	@Test
	@DisplayName("[성공] 신청서를 삭제한다.")
	void delete_success() throws Exception {

		mockMvc.perform(delete("/api/v1/proposals/{proposalId}", proposalId)
				.header(AUTHORIZATION, BEARER_TYPE + accessToken2))
			.andExpect(status().isOk())
			.andDo(document("proposal-delete",
				resource(
					builder()
						.tag(PROPOSAL)
						.summary("신청서 삭제 API")
						.description("자신의 신청서를 삭제할 수 있다.")
						.pathParameters(
							parameterWithName("proposalId").description("신청서 아이디")
						)
						.build()
				)
			));

	}
}