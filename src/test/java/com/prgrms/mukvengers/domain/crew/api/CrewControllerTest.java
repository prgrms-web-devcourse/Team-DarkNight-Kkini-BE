package com.prgrms.mukvengers.domain.crew.api;

import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.prgrms.mukvengers.base.ControllerTest;
import com.prgrms.mukvengers.domain.crew.dto.request.CreateCrewRequest;
import com.prgrms.mukvengers.domain.crew.dto.response.CrewResponse;
import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crew.repository.CrewRepository;
import com.prgrms.mukvengers.domain.store.model.Store;
import com.prgrms.mukvengers.domain.store.repository.StoreRepository;
import com.prgrms.mukvengers.domain.user.model.User;
import com.prgrms.mukvengers.utils.CrewObjectProvider;
import com.prgrms.mukvengers.utils.StoreObjectProvider;
import com.prgrms.mukvengers.utils.UserObjectProvider;

class CrewControllerTest extends ControllerTest {

	@Autowired
	private StoreRepository storeRepository;

	@Autowired
	private CrewRepository crewRepository;

	@Test
	@DisplayName("[성공]밥 모임을 저장한다.")
	void create_success() throws Exception {

		Store store = StoreObjectProvider.createStore("16618597");

		storeRepository.save(store);

		String mapStoreId = "16618597";

		CreateCrewRequest createCrewRequest = CrewObjectProvider.getCreateCrewRequest(mapStoreId);

		String jsonRequest = objectMapper.writeValueAsString(createCrewRequest);

		mockMvc.perform(post("/api/v1/crews")
				.contentType(APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, BEARER_TYPE + ACCESS_TOKEN)
				.content(jsonRequest))
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", containsString("/api/v1/crews")))
			.andExpect(redirectedUrlPattern("http://localhost:8080/api/v1/crews/*"))
			.andDo(print())
			.andDo(document("crew-create",
				requestFields(
					fieldWithPath("latitude").type(STRING).description("위도"),
					fieldWithPath("longitude").type(STRING).description("경도"),
					fieldWithPath("mapStoreId").type(STRING).description("지도 api 제공 id"),
					fieldWithPath("name").type(STRING).description("밥 모임 이름"),
					fieldWithPath("capacity").type(NUMBER).description("밥 모임 정원"),
					fieldWithPath("status").type(STRING).description("밥 모임 상태"),
					fieldWithPath("content").type(STRING).description("밥 모임 설명"),
					fieldWithPath("category").type(STRING).description("밥 모임 카테고리")
				)
			));

	}

	@Test
	@DisplayName("[성공] 맵 api 아이디로 해당 가게의 밥 모임을 전부 조회한다.")
	void findByMapStoreId_success() throws Exception {

		String mapStoreId = "16618597";

		User user = UserObjectProvider.createUser();

		userRepository.save(user);

		Store store = StoreObjectProvider.createStore(mapStoreId);

		storeRepository.save(store);

		List<Crew> crews = CrewObjectProvider.createCrews(user, store);

		crewRepository.saveAll(crews);

		mockMvc.perform(get("/api/v1/crews/{mapStoreId}", mapStoreId)
				.accept(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.responses").exists())
			.andDo(print())
			.andDo(document("crew-findByMapStoreId",
				pathParameters(
					parameterWithName("mapStoreId").description("api에서 사용하는 가게 아이디")
				),
				responseFields(
					fieldWithPath("responses.[].id").type(NUMBER).description("밥 모임 아이디"),
					fieldWithPath("responses.[].leader").type(OBJECT).description("밥 모임 방장 정보"),
					fieldWithPath("responses.[].leader.id").type(NUMBER).description("방장 아이디"),
					fieldWithPath("responses.[].leader.nickname").type(STRING).description("방장 닉네임"),
					fieldWithPath("responses.[].leader.profileImgUrl").type(STRING).description("방장 프로필"),
					fieldWithPath("responses.[].leader.introduction").type(STRING).description("방장 소개"),
					fieldWithPath("responses.[].leader.leaderCount").type(NUMBER).description("방장 횟수"),
					fieldWithPath("responses.[].leader.crewCount").type(NUMBER).description("방장 모임 참여 횟수"),
					fieldWithPath("responses.[].leader.tasteScore").type(NUMBER).description("방장 맛잘알 점수"),
					fieldWithPath("responses.[].leader.mannerScore").type(NUMBER).description("방장 매너온도"),
					fieldWithPath("responses.[].store").type(OBJECT).description("밥 모임 가게 정보"),
					fieldWithPath("responses.[].store.id").type(NUMBER).description("밥 모임 가게 아이디"),
					fieldWithPath("responses.[].store.latitude").type(STRING).description("밥 모임 가게 위도"),
					fieldWithPath("responses.[].store.longitude").type(STRING).description("밥 모임 가게 경도"),
					fieldWithPath("responses.[].store.mapStoreId").type(STRING).description("맵 api 아이디"),
					fieldWithPath("responses.[].name").type(STRING).description("밥 모임 이름"),
					fieldWithPath("responses.[].latitude").type(STRING).description("가게 위도"),
					fieldWithPath("responses.[].longitude").type(STRING).description("가게 경도"),
					fieldWithPath("responses.[].capacity").type(NUMBER).description("밥 모임 정원"),
					fieldWithPath("responses.[].status").type(STRING).description("밥 모임 상태"),
					fieldWithPath("responses.[].content").type(STRING).description("밥 모임 내용"),
					fieldWithPath("responses.[].category").type(STRING).description("밥 모임 카테고리")
				)
			));

	}

	@Test
	@DisplayName("[성공] 맵 api 아이디로 해당 가게의 밥 모임을 전부 조회한다.")
	void findByLocation_success() throws Exception {

		String mapStoreId = "16618597";

		User user = UserObjectProvider.createUser();

		userRepository.save(user);

		Store store = StoreObjectProvider.createStore(mapStoreId);

		storeRepository.save(store);

		List<Crew> crews = CrewObjectProvider.createCrews(user, store);

		crewRepository.saveAll(crews);

		String latitude = "35.75413579";
		String longitude = "-147.4654321321";

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("x", latitude);
		params.add("y", longitude);

		mockMvc.perform(get("/api/v1/crews")
				.params(params)
				.accept(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.responses").exists())
			.andDo(print())
			.andDo(document("crew-findByLocation",
				requestParameters(
					parameterWithName("x").description("사용자의 위도"),
					parameterWithName("y").description("가게 경도")),
				responseFields(
					fieldWithPath("responses.[].id").type(NUMBER).description("밥 모임 아이디"),
					fieldWithPath("responses.[].leader").type(OBJECT).description("밥 모임 방장 정보"),
					fieldWithPath("responses.[].leader.id").type(NUMBER).description("방장 아이디"),
					fieldWithPath("responses.[].leader.nickname").type(STRING).description("방장 닉네임"),
					fieldWithPath("responses.[].leader.profileImgUrl").type(STRING).description("방장 프로필"),
					fieldWithPath("responses.[].leader.introduction").type(STRING).description("방장 소개"),
					fieldWithPath("responses.[].leader.leaderCount").type(NUMBER).description("방장 횟수"),
					fieldWithPath("responses.[].leader.crewCount").type(NUMBER).description("방장 모임 참여 횟수"),
					fieldWithPath("responses.[].leader.tasteScore").type(NUMBER).description("방장 맛잘알 점수"),
					fieldWithPath("responses.[].leader.mannerScore").type(NUMBER).description("방장 매너온도"),
					fieldWithPath("responses.[].store").type(OBJECT).description("밥 모임 가게 정보"),
					fieldWithPath("responses.[].store.id").type(NUMBER).description("밥 모임 가게 아이디"),
					fieldWithPath("responses.[].store.latitude").type(STRING).description("밥 모임 가게 위도"),
					fieldWithPath("responses.[].store.longitude").type(STRING).description("밥 모임 가게 경도"),
					fieldWithPath("responses.[].store.mapStoreId").type(STRING).description("맵 api 아이디"),
					fieldWithPath("responses.[].name").type(STRING).description("밥 모임 이름"),
					fieldWithPath("responses.[].latitude").type(STRING).description("가게 위도"),
					fieldWithPath("responses.[].longitude").type(STRING).description("가게 경도"),
					fieldWithPath("responses.[].capacity").type(NUMBER).description("밥 모임 정원"),
					fieldWithPath("responses.[].status").type(STRING).description("밥 모임 상태"),
					fieldWithPath("responses.[].content").type(STRING).description("밥 모임 내용"),
					fieldWithPath("responses.[].category").type(STRING).description("밥 모임 카테고리")
				)
			));
	}
}