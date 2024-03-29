package com.prgrms.mukvengers.domain.notification.api;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.*;
import static com.epages.restdocs.apispec.ResourceDocumentation.*;
import static com.epages.restdocs.apispec.ResourceSnippetParameters.*;
import static com.prgrms.mukvengers.domain.notification.model.vo.NotificationType.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.prgrms.mukvengers.base.ControllerTest;
import com.prgrms.mukvengers.domain.notification.dto.response.NotificationResponse;
import com.prgrms.mukvengers.domain.notification.dto.response.NotificationResponses;
import com.prgrms.mukvengers.domain.notification.service.NotificationService;

class NotificationControllerTest extends ControllerTest {

	@Autowired
	private NotificationController notificationController;

	@MockBean
	private NotificationService notificationService;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("[성공] SSE 연결을 진행한다.")
	public void subscribe() throws Exception {
		given(notificationService.subscribe(anyLong(), anyString())).willReturn(new SseEmitter());

		//when
		ResultActions result = mockMvc.perform(get("/subscribe")
			.header(AUTHORIZATION, BEARER_TYPE + accessToken1)
			.header("Last-Event-ID", "1_7598137456")
			.accept(MediaType.TEXT_EVENT_STREAM)
		);

		//then
		result
			.andExpect(status().isOk())
			.andDo(print())
			.andDo(document("SSE 연결",
					resource(
						builder()
							.tag(NOTIFICATION)
							.summary("SSE 연결 API")
							.description("SSE 연결을 시도합니다. 연결한 유저는 알림을 수신할 수 있습니다.")
							.requestHeaders(
								headerWithName(AUTHORIZATION).description("accessToken"),
								headerWithName("Last-Event-ID").description("마지막으로 수신한 emitter ID")
							)
							.build()
					)
				)
			);
	}

	@Test
	@DisplayName("[성공] 로그인 한 유저의 모든 알림 조회")
	void findAllNotification() throws Exception {
		// given
		List<NotificationResponse> notificationList = Arrays.asList(
			new NotificationResponse(10L, INFO.name(), "새로운 알림이 도착했습니다.", LocalDateTime.now().minusDays(1), false),
			new NotificationResponse(12L, INFO.name(), "새로운 알림이 도착했습니다.", LocalDateTime.now().minusDays(3), false),
			new NotificationResponse(14L, INFO.name(), "새로운 알림이 도착했습니다.", LocalDateTime.now().minusDays(5), false),
			new NotificationResponse(16L, INFO.name(), "새로운 알림이 도착했습니다.", LocalDateTime.now().minusDays(7), false)
		);
		NotificationResponses notificationsResponse = new NotificationResponses(notificationList, 2L);
		given(notificationService.findAllById(any())).willReturn(notificationsResponse);

		// when
		ResultActions result = mockMvc.perform(
			get("/notifications")
				.header(AUTHORIZATION, BEARER_TYPE + accessToken1)
				.accept(MediaType.APPLICATION_JSON)
		);

		// then
		result.andExpect(status().isOk())
			.andDo(print())
			.andDo(document("유저의 알림 목록 조회",
					resource(
						builder()
							.tag(NOTIFICATION)
							.summary("유저의 알림 목록 조회")
							.description("유저가 수신한 알림들을 조회합니다.")
							.requestHeaders(
								headerWithName(AUTHORIZATION).description("accessToken")
							)
							.responseFields(
								fieldWithPath("notifications.[].id").type(NUMBER).description("알림의 고유 ID"),
								fieldWithPath("notifications.[].type").type(STRING).description("알림 타입"),
								fieldWithPath("notifications.[].content").type(STRING).description("알림 내용"),
								fieldWithPath("notifications.[].createdAt").type(ARRAY).description("알림 생성일자"),
								fieldWithPath("notifications.[].isRead").type(BOOLEAN).description("읽음 여부"),
								fieldWithPath("unReadCount").type(NUMBER).description("읽지 않은 알림의 개수")
							)
							.build()
					)
				)
			);
	}

	@Test
	@DisplayName("[성공] 알림 조회 시 읽음 상태로 변경한다.")
	void readNotification() throws Exception {
		// given & when
		ResultActions result = mockMvc.perform(
			patch("/notifications/{id}", 1)
				.header(AUTHORIZATION, BEARER_TYPE + accessToken1)
		);

		// then
		result.andExpect(status().isNoContent())
			.andDo(document("특정 알림 읽음처리",
					resource(
						builder()
							.tag(NOTIFICATION)
							.summary("유저의 알림 읽음처리")
							.description("유저가 수신한 알림들을 읽음처리합니다.")
							.requestHeaders(
								headerWithName(AUTHORIZATION).description("accessToken")
							)
							.pathParameters(
								parameterWithName("id").description("읽음 처리할 알림의 ID")
							)
							.build()
					)
				)
			);
	}
}