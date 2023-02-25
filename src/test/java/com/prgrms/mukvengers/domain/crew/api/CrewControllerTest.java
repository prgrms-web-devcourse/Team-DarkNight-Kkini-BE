package com.prgrms.mukvengers.domain.crew.api;

import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import com.prgrms.mukvengers.base.ControllerTest;
import com.prgrms.mukvengers.domain.crew.dto.CreateCrewRequest;
import com.prgrms.mukvengers.domain.store.model.Store;
import com.prgrms.mukvengers.domain.store.repository.StoreRepository;
import com.prgrms.mukvengers.utils.StoreObjectProvider;

class CrewControllerTest extends ControllerTest {

	@Autowired
	private StoreRepository storeRepository;

	@Test
	@DisplayName("[성공]밥 모임을 저장한다.")
	void create_success() throws Exception {

		Store store = StoreObjectProvider.createStore("16618597");

		storeRepository.save(store);

		String mapStoreId = "16618597";
		String name = "원정대이름";
		String latitude = "35.75413579";
		String longitude = "-147.4654321321";
		Integer capacity = 5;
		String status = "모집중";
		String content = "저는 백엔드 개발자 입니다. 프론트 엔드 개발자 구해요";
		String category = "조용한";

		CreateCrewRequest createCrewRequest = new CreateCrewRequest(
			mapStoreId,
			name,
			latitude,
			longitude,
			capacity,
			status,
			content,
			category
		);

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
}