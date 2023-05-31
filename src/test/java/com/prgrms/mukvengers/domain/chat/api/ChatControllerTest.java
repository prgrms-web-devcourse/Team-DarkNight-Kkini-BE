package com.prgrms.mukvengers.domain.chat.api;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.*;
import static com.epages.restdocs.apispec.ResourceDocumentation.*;
import static com.epages.restdocs.apispec.ResourceSnippetParameters.*;
import static com.prgrms.mukvengers.utils.ChatObjectProvider.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.payload.JsonFieldType;

import com.epages.restdocs.apispec.Schema;
import com.prgrms.mukvengers.base.ControllerTest;
import com.prgrms.mukvengers.domain.chat.dto.request.ChatRequest;
import com.prgrms.mukvengers.domain.chat.service.ChatService;

class ChatControllerTest extends ControllerTest {

	private static final String CHAT = "채팅 API";
	private static final Schema GET_CHAT_RESPONSE = new Schema("getChatResponse");

	@Autowired
	private ChatService chatService;

	@BeforeEach
	void setUp() {
		Map<String, Object> simpSessionAttributes = new HashMap<>();
		simpSessionAttributes.put("username", "testUser");
		simpSessionAttributes.put("profileImgUrl", "testProfileImgUrl");
		ChatRequest chatRequest = createChatRequest(savedUser1Id);
		for (int i = 0; i < 10; i++) {
			chatService.save(chatRequest, 1L, simpSessionAttributes);
		}
	}

	@Test
	@DisplayName("[성공] 채팅 내역을 불러올 수 있다 - v2 : 페이지네이션 버전")
	void getChattingListV2Test_success() throws Exception {

		mockMvc.perform(get("/api/v2/crews/{crewId}/chats", 1L)
				.queryParam("page", "0")
				.queryParam("size", "10")
				.header(AUTHORIZATION, BEARER_TYPE + accessToken1)
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.chats.content[0].id").exists())
			.andExpect(jsonPath("$.data.chats.content[0].userId").exists())
			.andExpect(jsonPath("$.data.chats.content[0].username").exists())
			.andExpect(jsonPath("$.data.chats.content[0].profileImgUrl").exists())
			.andExpect(jsonPath("$.data.chats.content[0].type").exists())
			.andExpect(jsonPath("$.data.chats.content[0].createdAt").exists())
			.andExpect(jsonPath("$.data.chats.content[0].content").exists())
			.andDo(print())
			.andDo(document("채팅방 내역 조회 - V2",
				resource(
					builder()
						.tag(CHAT)
						.summary("채팅방 조회 API")
						.description("채팅방 내의 대화 내역 조회합니다. v2의 경우는 페이지네이션이 적용되어 있습니다.")
						.pathParameters(
							parameterWithName("crewId").description("조회할 채팅방 번호 - 모임 아이디"))
						.requestParameters(
							parameterWithName("page").description("조회할 페이지 번호"),
							parameterWithName("size").description("조회할 페이지 사이즈"))
						.responseFields(
							fieldWithPath("data.chats.content[].id").type(JsonFieldType.NUMBER).description("채팅방 아이디"),
							fieldWithPath("data.chats.content[].userId").type(JsonFieldType.NUMBER)
								.description("유저 아이디"),
							fieldWithPath("data.chats.content[].username").type(JsonFieldType.STRING)
								.description("유저 이름"),
							fieldWithPath("data.chats.content[].profileImgUrl").type(JsonFieldType.STRING)
								.description("프로필 이미지 url"),
							fieldWithPath("data.chats.content[].type").type(JsonFieldType.STRING).description("메시지 타입"),
							fieldWithPath("data.chats.content[].createdAt").type(JsonFieldType.ARRAY)
								.description("메시지 생성 시간"),
							fieldWithPath("data.chats.content[].content").type(JsonFieldType.STRING)
								.description("채팅 내용"),
							fieldWithPath("data.chats.pageable.sort.empty").type(JsonFieldType.BOOLEAN)
								.description("빈 페이지 여부"),
							fieldWithPath("data.chats.pageable.sort.sorted").type(JsonFieldType.BOOLEAN)
								.description("페이지 정렬 여부"),
							fieldWithPath("data.chats.pageable.sort.unsorted").type(JsonFieldType.BOOLEAN)
								.description("페이지 비정렬 여부"),
							fieldWithPath("data.chats.pageable.offset").type(JsonFieldType.NUMBER)
								.description("페이지 오프셋"),
							fieldWithPath("data.chats.pageable.pageNumber").type(JsonFieldType.NUMBER)
								.description("페이지 번호"),
							fieldWithPath("data.chats.pageable.pageSize").type(JsonFieldType.NUMBER)
								.description("한 페이지에 나타내는 원소 수"),
							fieldWithPath("data.chats.pageable.paged").type(JsonFieldType.BOOLEAN)
								.description("페이지 정보 포함 여부"),
							fieldWithPath("data.chats.pageable.unpaged").type(JsonFieldType.BOOLEAN)
								.description("페이지 정보 비포함 여부"),
							fieldWithPath("data.chats.last").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
							fieldWithPath("data.chats.size").type(JsonFieldType.NUMBER).description("페이지 사이즈"),
							fieldWithPath("data.chats.number").type(JsonFieldType.NUMBER).description("페이지 번호"),
							fieldWithPath("data.chats.sort.empty").type(JsonFieldType.BOOLEAN).description("빈 페이지 여부"),
							fieldWithPath("data.chats.sort.sorted").type(JsonFieldType.BOOLEAN)
								.description("페이지 정렬 여부"),
							fieldWithPath("data.chats.sort.unsorted").type(JsonFieldType.BOOLEAN)
								.description("페이지 비정렬 여부"),
							fieldWithPath("data.chats.first").type(JsonFieldType.BOOLEAN).description("첫 번째 페이지 여부"),
							fieldWithPath("data.chats.numberOfElements").type(JsonFieldType.NUMBER)
								.description("페이지 원소 개수"),
							fieldWithPath("data.chats.empty").type(JsonFieldType.BOOLEAN).description("빈 페이지 여부"),
							fieldWithPath("data.chats.totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 개수"),
							fieldWithPath("data.chats.totalElements").type(JsonFieldType.NUMBER)
								.description("전체 데이터 개수"))
						.responseSchema(GET_CHAT_RESPONSE)
						.build()
				)
			));
	}

	@Test
	@DisplayName("[성공] 채팅 내역을 불러올 수 있다 - V1 : 일반 버전")
	void getChattingListV1Test_success() throws Exception {

		mockMvc.perform(get("/api/v1/crews/{crewId}/chats", 1L)
				.header(AUTHORIZATION, BEARER_TYPE + accessToken1))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.chats[0].id").exists())
			.andExpect(jsonPath("$.data.chats[0].userId").exists())
			.andExpect(jsonPath("$.data.chats[0].username").exists())
			.andExpect(jsonPath("$.data.chats[0].profileImgUrl").exists())
			.andExpect(jsonPath("$.data.chats[0].type").exists())
			.andExpect(jsonPath("$.data.chats[0].createdAt").exists())
			.andExpect(jsonPath("$.data.chats[0].content").exists())
			.andDo(print())
			.andDo(document("채팅방 내역 조회 - V1",
				resource(
					builder()
						.tag(CHAT)
						.summary("채팅방 조회 API")
						.description("채팅방 내의 대화 내역 조회합니다. v2의 경우는 페이지네이션이 적용되어 있습니다.")
						.pathParameters(
							parameterWithName("crewId").description("조회할 채팅방 번호 - 모임 아이디"))
						.responseFields(
							fieldWithPath("data.chats[].id").type(JsonFieldType.NUMBER).description("채팅방 아이디"),
							fieldWithPath("data.chats[].userId").type(JsonFieldType.NUMBER).description("유저 아이디"),
							fieldWithPath("data.chats[].username").type(JsonFieldType.STRING).description("유저 이름"),
							fieldWithPath("data.chats[].profileImgUrl").type(JsonFieldType.STRING)
								.description("프로필 이미지 url"),
							fieldWithPath("data.chats[].type").type(JsonFieldType.STRING).description("메시지 타입"),
							fieldWithPath("data.chats[].createdAt").type(JsonFieldType.ARRAY).description("메시지 생성 시간"),
							fieldWithPath("data.chats[].content").type(JsonFieldType.STRING).description("채팅 내용"))
						.responseSchema(GET_CHAT_RESPONSE)
						.build()
				)
			));
	}

}