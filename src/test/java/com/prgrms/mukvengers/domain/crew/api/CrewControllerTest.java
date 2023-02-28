package com.prgrms.mukvengers.domain.crew.api;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.*;
import static com.epages.restdocs.apispec.ResourceDocumentation.*;
import static com.epages.restdocs.apispec.ResourceSnippetParameters.*;
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
import com.prgrms.mukvengers.utils.CrewObjectProvider;

class CrewControllerTest extends ControllerTest {

	public static final Schema CREATE_CREW_REQUEST = new Schema("createCrewRequest");
	public static final Schema CREW_PAGE_RESPONSE = new Schema("crewPageResponse");
	public static final Schema FIND_BY_USER_LOCATION_CREW_REQUEST = new Schema("findByUserLocationCrewRequest");
	public static final Schema CREW_RESPONSE = new Schema("crewResponse");
	public static final Schema UPDATE_CREW_REQUEST = new Schema("updateCrewRequest");

	@Test
	@DisplayName("[성공]밥 모임을 저장한다.")
	void create_success() throws Exception {

		CreateCrewRequest createCrewRequest = CrewObjectProvider.getCreateCrewRequest(savedStore.getMapStoreId());

		String jsonRequest = objectMapper.writeValueAsString(createCrewRequest);

		mockMvc.perform(post("/api/v1/crews")
				.contentType(APPLICATION_JSON)
				.header(AUTHORIZATION, BEARER_TYPE + accessToken)
				.content(jsonRequest))
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", containsString("/api/v1/crews")))
			.andExpect(redirectedUrlPattern("http://localhost:8080/api/v1/crews/*"))
			.andDo(print())
			.andDo(document("모임 생성",
				resource(
					builder()
						.tag(CREW)
						.summary("모임 생성 API")
						.description("모임을 생성합니다.")
						.requestSchema(CREATE_CREW_REQUEST)
						.requestFields(
							fieldWithPath("latitude").type(STRING).description("위도"),
							fieldWithPath("longitude").type(STRING).description("경도"),
							fieldWithPath("mapStoreId").type(STRING).description("지도 api 제공 id"),
							fieldWithPath("name").type(STRING).description("밥 모임 이름"),
							fieldWithPath("capacity").type(NUMBER).description("밥 모임 정원"),
							fieldWithPath("promiseTime").type(STRING).description("약속 시간"),
							fieldWithPath("status").type(STRING).description("밥 모임 상태"),
							fieldWithPath("content").type(STRING).description("밥 모임 설명"),
							fieldWithPath("category").type(STRING).description("밥 모임 카테고리"))
						.responseHeaders(
							headerWithName("Location").description("조회해볼 수 있는 요청 주소"))
						.build()
				)
			));

	}

	@Test
	@DisplayName("[성공] 맵 api 아이디로 해당 가게의 밥 모임을 전부 조회한다.")
	void findByMapStoreId_success() throws Exception {

		List<Crew> crews = CrewObjectProvider.createCrews(savedUser, savedStore);

		crewRepository.saveAll(crews);

		Integer page = 0;

		Integer size = 5;

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("page", String.valueOf(page));
		params.add("size", String.valueOf(size));

		mockMvc.perform(get("/api/v1/crews/{mapStoreId}", savedStore.getMapStoreId())
				.params(params)
				.header(AUTHORIZATION, BEARER_TYPE + accessToken)
				.accept(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data").exists())
			.andDo(print())
			.andDo(document("crew-findByMapStoreId",
				resource(
					builder()
						.tag(CREW)
						.summary("가게 기반 밥 모임 조회 API")
						.description("맵 api 아이디로 밥 모임을 조회합니다.")
						.pathParameters(
							parameterWithName("mapStoreId").description("api에서 사용하는 가게 아이디"))
						.requestParameters(
							parameterWithName("page").description("현재 페이지 번호"),
							parameterWithName("size").description("한번에 가져올 데이터 사이즈"))
						.responseSchema(CREW_PAGE_RESPONSE)
						.responseFields(
							fieldWithPath("data.responses.content.[].id").type(NUMBER).description("밥 모임 아이디"),
							fieldWithPath("data.responses.content.[].leader").type(OBJECT).description("밥 모임 방장 정보"),
							fieldWithPath("data.responses.content.[].leader.id").type(NUMBER).description("방장 아이디"),
							fieldWithPath("data.responses.content.[].leader.nickname").type(STRING)
								.description("방장 닉네임"),
							fieldWithPath("data.responses.content.[].leader.profileImgUrl").type(STRING)
								.description("방장 프로필"),
							fieldWithPath("data.responses.content.[].leader.introduction").type(STRING)
								.description("방장 소개"),
							fieldWithPath("data.responses.content.[].leader.leaderCount").type(NUMBER)
								.description("방장 횟수"),
							fieldWithPath("data.responses.content.[].leader.crewCount").type(NUMBER)
								.description("방장 모임 참여 횟수"),
							fieldWithPath("data.responses.content.[].leader.tasteScore").type(NUMBER)
								.description("방장 맛잘알 점수"),
							fieldWithPath("data.responses.content.[].leader.mannerScore").type(NUMBER)
								.description("방장 매너온도"),
							fieldWithPath("data.responses.content.[].store").type(OBJECT).description("밥 모임 가게 정보"),
							fieldWithPath("data.responses.content.[].store.id").type(NUMBER).description("밥 모임 가게 아이디"),
							fieldWithPath("data.responses.content.[].store.latitude").type(STRING)
								.description("밥 모임 가게 위도"),
							fieldWithPath("data.responses.content.[].store.longitude").type(STRING)
								.description("밥 모임 가게 경도"),
							fieldWithPath("data.responses.content.[].store.mapStoreId").type(STRING)
								.description("맵 api 아이디"),
							fieldWithPath("data.responses.content.[].name").type(STRING).description("밥 모임 이름"),
							fieldWithPath("data.responses.content.[].latitude").type(STRING).description("가게 위도"),
							fieldWithPath("data.responses.content.[].longitude").type(STRING).description("가게 경도"),
							fieldWithPath("data.responses.content.[].capacity").type(NUMBER).description("밥 모임 정원"),
							fieldWithPath("data.responses.content.[].promiseTime").type(ARRAY).description("약속 시간"),
							fieldWithPath("data.responses.content.[].status").type(STRING).description("밥 모임 상태"),
							fieldWithPath("data.responses.content.[].content").type(STRING).description("밥 모임 내용"),
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

		List<Crew> crews = CrewObjectProvider.createCrews(savedUser, savedStore);

		crewRepository.saveAll(crews);

		String latitude = "35.75413579";
		String longitude = "-147.4654321321";

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("latitude", latitude);
		params.add("longitude", longitude);

		mockMvc.perform(get("/api/v1/crews")
				.params(params)
				.header(AUTHORIZATION, BEARER_TYPE + accessToken)
				.accept(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data").exists())
			.andDo(print())
			.andDo(document("crew-findByLocation",
				resource(
					builder()
						.tag(CREW)
						.summary("위치 기반 밥 모임 조회 API")
						.description("사용자 위치로 특정 거리 안에 있는 모임을 조회합니다.")
						.requestSchema(FIND_BY_USER_LOCATION_CREW_REQUEST)
						.requestParameters(
							parameterWithName("latitude").description("사용자의 위도"),
							parameterWithName("longitude").description("사용자의 경도"))
						.responseSchema(CREW_RESPONSE)
						.responseFields(
							fieldWithPath("data.responses.[].id").type(NUMBER).description("밥 모임 아이디"),
							fieldWithPath("data.responses.[].leader").type(OBJECT).description("밥 모임 방장 정보"),
							fieldWithPath("data.responses.[].leader.id").type(NUMBER).description("방장 아이디"),
							fieldWithPath("data.responses.[].leader.nickname").type(STRING).description("방장 닉네임"),
							fieldWithPath("data.responses.[].leader.profileImgUrl").type(STRING).description("방장 프로필"),
							fieldWithPath("data.responses.[].leader.introduction").type(STRING).description("방장 소개"),
							fieldWithPath("data.responses.[].leader.leaderCount").type(NUMBER).description("방장 횟수"),
							fieldWithPath("data.responses.[].leader.crewCount").type(NUMBER).description("방장 모임 참여 횟수"),
							fieldWithPath("data.responses.[].leader.tasteScore").type(NUMBER).description("방장 맛잘알 점수"),
							fieldWithPath("data.responses.[].leader.mannerScore").type(NUMBER).description("방장 매너온도"),
							fieldWithPath("data.responses.[].store").type(OBJECT).description("밥 모임 가게 정보"),
							fieldWithPath("data.responses.[].store.id").type(NUMBER).description("밥 모임 가게 아이디"),
							fieldWithPath("data.responses.[].store.latitude").type(STRING).description("밥 모임 가게 위도"),
							fieldWithPath("data.responses.[].store.longitude").type(STRING).description("밥 모임 가게 경도"),
							fieldWithPath("data.responses.[].store.mapStoreId").type(STRING).description("맵 api 아이디"),
							fieldWithPath("data.responses.[].name").type(STRING).description("밥 모임 이름"),
							fieldWithPath("data.responses.[].latitude").type(STRING).description("가게 위도"),
							fieldWithPath("data.responses.[].longitude").type(STRING).description("가게 경도"),
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

		Crew crew = CrewObjectProvider.createCrew(savedUser, savedStore);

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