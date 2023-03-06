package com.prgrms.mukvengers.domain.crew.api;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.*;
import static com.epages.restdocs.apispec.ResourceDocumentation.*;
import static com.epages.restdocs.apispec.ResourceSnippetParameters.*;
import static com.prgrms.mukvengers.domain.crew.model.vo.Status.*;
import static com.prgrms.mukvengers.utils.CrewMemberObjectProvider.*;
import static com.prgrms.mukvengers.utils.CrewObjectProvider.*;
import static org.hamcrest.Matchers.*;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.epages.restdocs.apispec.Schema;
import com.prgrms.mukvengers.base.ControllerTest;
import com.prgrms.mukvengers.domain.crew.dto.request.CreateCrewRequest;
import com.prgrms.mukvengers.domain.crew.dto.request.UpdateStatusRequest;
import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crewmember.model.CrewMember;
import com.prgrms.mukvengers.domain.crewmember.model.vo.Role;
import com.prgrms.mukvengers.utils.CrewObjectProvider;

class CrewControllerTest extends ControllerTest {

	public static final Schema CREATE_CREW_REQUEST = new Schema("createCrewRequest");
	public static final Schema FIND_BY_CREW_ID_CREW_REQUEST = new Schema("findByCrewIdCrewRequest");
	public static final Schema CREW_PAGE_RESPONSE = new Schema("crewPageResponse");
	public static final Schema FIND_BY_USER_LOCATION_CREW_REQUEST = new Schema("findByUserLocationCrewRequest");
	public static final Schema CREW_RESPONSE = new Schema("crewResponse");
	public static final Schema UPDATE_CREW_REQUEST = new Schema("updateCrewRequest");
	public static final Schema FIND_BY_USER_ID_CREW_REQUEST = new Schema("findByUserIdCrewRequest");

	@Test
	@DisplayName("[성공]밥 모임을 저장하고 모임을 만든 유저는 모임원이 되며 방장 역할을 가진다.")
	void create_success() throws Exception {

		CreateCrewRequest createCrewRequest = CrewObjectProvider.getCreateCrewRequest(savedStore.getPlaceId());

		String jsonRequest = objectMapper.writeValueAsString(createCrewRequest);

		mockMvc.perform(post("/api/v1/crews")
				.contentType(APPLICATION_JSON)
				.header(AUTHORIZATION, BEARER_TYPE + accessToken)
				.content(jsonRequest))
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", containsString("/api/v1/crews")))
			.andExpect(redirectedUrlPattern("/api/v1/crews/*"))
			.andDo(print())
			.andDo(document("모임 생성",
				resource(
					builder()
						.tag(CREW)
						.summary("모임 생성 API")
						.description("모임을 생성합니다. 생성한 유저는 모임원이 되고 방장 역할을 가집니다.")
						.requestSchema(CREATE_CREW_REQUEST)
						.requestFields(
							fieldWithPath("latitude").type(STRING).description("위도"),
							fieldWithPath("longitude").type(STRING).description("경도"),
							fieldWithPath("mapStoreId").type(STRING).description("지도 api 제공 id"),
							fieldWithPath("name").type(STRING).description("밥 모임 이름"),
							fieldWithPath("capacity").type(NUMBER).description("밥 모임 정원"),
							fieldWithPath("promiseTime").type(STRING).description("약속 시간"),
							fieldWithPath("content").type(STRING).description("밥 모임 설명"),
							fieldWithPath("category").type(STRING).description("밥 모임 카테고리"))
						.responseHeaders(
							headerWithName("Location").description("조회해볼 수 있는 요청 주소"))
						.build()
				)
			));

	}

	@Test
	@DisplayName("[성공] 모임 아이디로 모임을 조회한다.")
	void getById_success() throws Exception {

		Crew crew = CrewObjectProvider.createCrew(savedStore, RECRUITING);

		crewRepository.save(crew);

		Long crewId = crew.getId();

		mockMvc.perform(get("/api/v1/crews/{crewId}", crewId)
				.header(AUTHORIZATION, BEARER_TYPE + accessToken)
				.accept(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data").exists())
			.andDo(print())
			.andDo(document("crew-getById",
				resource(
					builder()
						.tag(CREW)
						.summary("모임 아이디로 모임 조회 API")
						.description("모임 아이디로 모임을 단건 조회 합니다.")
						.requestSchema(FIND_BY_CREW_ID_CREW_REQUEST)
						.pathParameters(
							parameterWithName("crewId").description("모임 아이디"))
						.responseSchema(CREW_RESPONSE)
						.responseFields(
							fieldWithPath("data.currentMember").type(NUMBER).description("밥 모임 현재 인원"),
							fieldWithPath("data.id").type(NUMBER).description("밥 모임 아이디"),
							fieldWithPath("data.name").type(STRING).description("밥 모임 이름"),
							fieldWithPath("data.capacity").type(NUMBER).description("밥 모임 정원"),
							fieldWithPath("data.promiseTime").type(ARRAY).description("약속 시간"),
							fieldWithPath("data.status").type(STRING).description("밥 모임 상태"),
							fieldWithPath("data.content").type(STRING).description("밥 모임 내용"),
							fieldWithPath("data.category").type(STRING).description("밥 모임 카테고리"))
						.build()
				)
			));
	}

	@Test
	@DisplayName("[성공] 사용자가 참여한 모임을 조회한다.")
	void getByUserId_success() throws Exception {

		List<Crew> crews = createCrews(savedStore);
		crewRepository.saveAll(crews);

		crews.forEach(crew -> {
			CrewMember crewMember = createCrewMember(savedUserId, crew, Role.MEMBER);
			crewMemberRepository.save(crewMember);
		});

		mockMvc.perform(get("/api/v1/crews/me")
				.header(AUTHORIZATION, BEARER_TYPE + accessToken)
				.accept(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data").exists())
			.andDo(print())
			.andDo(document("crew-getByUserId",
				resource(
					builder()
						.tag(CREW)
						.summary("사용자가 참여한 모든 모임 조회 API")
						.description(
							"사용자가 참여한 모든 모임을 조회합니다. "
								+ "현재 모집 중인 모임과 모집종료된 모임 모두 조회하며 모집중인 데이터가 먼저 정렬되고 그후 모집 종료된 데이터가 옵니다.")
						.requestSchema(FIND_BY_USER_ID_CREW_REQUEST)
						.responseSchema(CREW_RESPONSE)
						.responseFields(
							fieldWithPath("data.profileImgUrl").type(STRING).description("사용자의 프로필 이미지"),
							fieldWithPath("data.responses.[].currentMember").type(NUMBER).description("밥 모임 현재 인원"),
							fieldWithPath("data.responses.[].id").type(NUMBER).description("밥 모임 아이디"),
							fieldWithPath("data.responses.[].name").type(STRING).description("밥 모임 이름"),
							fieldWithPath("data.responses.[].capacity").type(NUMBER).description("밥 모임 정원"),
							fieldWithPath("data.responses.[].promiseTime").type(ARRAY).description("약속 시간"),
							fieldWithPath("data.responses.[].status").type(STRING).description("밥 모임 상태"),
							fieldWithPath("data.responses.[].content").type(STRING).description("밥 모임 내용"),
							fieldWithPath("data.responses.[].category").type(STRING).description("밥 모임 카테고리"))
						.build()
				)
			));
	}

	@Test
	@DisplayName("[성공] 맵 api 아이디로 해당 가게의 밥 모임을 전부 조회한다.")
	void findByMapStoreId_success() throws Exception {

		List<Crew> crews = CrewObjectProvider.createCrews(savedStore);

		crewRepository.saveAll(crews);

		Integer page = 0;

		Integer size = 5;

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("page", String.valueOf(page));
		params.add("size", String.valueOf(size));

		mockMvc.perform(get("/api/v1/crews/page/{placeId}", savedStore.getPlaceId())
				.params(params)
				.header(AUTHORIZATION, BEARER_TYPE + accessToken)
				.accept(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data").exists())
			.andDo(print())
			.andDo(document("crew-getByPlaceId",
				resource(
					builder()
						.tag(CREW)
						.summary("가게 기반 밥 모임 조회 API")
						.description("맵 api 아이디로 밥 모임을 조회합니다.")
						.pathParameters(
							parameterWithName("placeId").description("api에서 사용하는 가게 아이디"))
						.requestParameters(
							parameterWithName("page").description("현재 페이지 번호"),
							parameterWithName("size").description("한번에 가져올 데이터 사이즈"))
						.responseSchema(CREW_PAGE_RESPONSE)
						.responseFields(
							fieldWithPath("data.responses.content.[].id").type(NUMBER).description("밥 모임 아이디"),
							fieldWithPath("data.responses.content.[].name").type(STRING).description("밥 모임 이름"),
							fieldWithPath("data.responses.content.[].capacity").type(NUMBER).description("밥 모임 정원"),
							fieldWithPath("data.responses.content.[].promiseTime").type(ARRAY).description("약속 시간"),
							fieldWithPath("data.responses.content.[].status").type(STRING).description("밥 모임 상태"),
							fieldWithPath("data.responses.content.[].content").type(STRING).description("밥 모임 내용"),
							fieldWithPath("data.responses.content.[]currentMember").type(NUMBER)
								.description("밥 모임 현재 인원"),
							fieldWithPath("data.responses.content.[].category").type(STRING).description("밥 모임 카테고리"),
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
	@DisplayName("[성공] 사용자의 위도, 경도로 특정 범위 안에 있는 밥 모임을 모드 조회한다.")
	void findByLocation_success() throws Exception {

		List<Crew> crews = CrewObjectProvider.createCrews(savedStore);

		crewRepository.saveAll(crews);

		String latitude = "35.75413579";
		String longitude = "-147.4654321321";
		String distance = "500";

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("latitude", latitude);
		params.add("longitude", longitude);
		params.add("distance", distance);

		mockMvc.perform(get("/api/v1/crews")
				.params(params)
				.header(AUTHORIZATION, BEARER_TYPE + accessToken)
				.accept(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data").exists())
			.andDo(print())
			.andDo(document("crew-getByLocation",
				resource(
					builder()
						.tag(CREW)
						.summary("위치 기반 밥 모임 조회 API")
						.description("사용자 위치로 특정 거리 안에 있는 모임을 조회합니다.")
						.requestSchema(FIND_BY_USER_LOCATION_CREW_REQUEST)
						.requestParameters(
							parameterWithName("latitude").description("사용자의 위도"),
							parameterWithName("longitude").description("사용자의 경도"),
							parameterWithName("distance").description("모임을 찾을 범위 거리"))
						.responseSchema(CREW_RESPONSE)
						.responseFields(
							fieldWithPath("data.responses.[].currentMember").type(NUMBER).description("밥 모임 현재 인원"),
							fieldWithPath("data.responses.[].id").type(NUMBER).description("밥 모임 아이디"),
							fieldWithPath("data.responses.[].name").type(STRING).description("밥 모임 이름"),
							fieldWithPath("data.responses.[].capacity").type(NUMBER).description("밥 모임 정원"),
							fieldWithPath("data.responses.[].promiseTime").type(ARRAY).description("약속 시간"),
							fieldWithPath("data.responses.[].status").type(STRING).description("밥 모임 상태"),
							fieldWithPath("data.responses.[].content").type(STRING).description("밥 모임 내용"),
							fieldWithPath("data.responses.[].category").type(STRING).description("밥 모임 카테고리"))
						.build()
				)
			));
	}

	@Test
	@DisplayName("[성공]밥 모임 상태를 변경한다.")
	void updateStatus_success() throws Exception {

		Crew crew = CrewObjectProvider.createCrew(savedStore, RECRUITING);

		crewRepository.save(crew);

		String status = "모집종료";

		UpdateStatusRequest updateStatusRequest = new UpdateStatusRequest(crew.getId(), status);

		String jsonRequest = objectMapper.writeValueAsString(updateStatusRequest);

		mockMvc.perform(patch("/api/v1/crews")
				.contentType(APPLICATION_JSON)
				.header(AUTHORIZATION, BEARER_TYPE + accessToken)
				.content(jsonRequest))
			.andExpect(status().isOk())
			.andDo(print())
			.andDo(document("crew-updateStatus",
				resource(
					builder()
						.tag(CREW)
						.summary("모임 상태 변경 API")
						.description("밥 모임의 상태를 변경합니다.")
						.requestSchema(UPDATE_CREW_REQUEST)
						.requestFields(
							fieldWithPath("crewId").type(NUMBER).description("밥 모임 아이디"),
							fieldWithPath("status").type(STRING).description("밥 모임 상태"))
						.build()
				)
			));

	}
}