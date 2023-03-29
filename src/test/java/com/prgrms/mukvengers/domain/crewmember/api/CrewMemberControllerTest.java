package com.prgrms.mukvengers.domain.crewmember.api;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.*;
import static com.epages.restdocs.apispec.ResourceDocumentation.*;
import static com.epages.restdocs.apispec.ResourceSnippetParameters.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.prgrms.mukvengers.base.ControllerTest;
import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crewmember.model.CrewMember;
import com.prgrms.mukvengers.domain.crewmember.model.vo.CrewMemberRole;
import com.prgrms.mukvengers.utils.CrewMemberObjectProvider;
import com.prgrms.mukvengers.utils.CrewObjectProvider;

class CrewMemberControllerTest extends ControllerTest {

	private Long crewId;
	private Long crewMemberId;

	@BeforeEach
	void setUp() {

		Crew crew = CrewObjectProvider.createCrew(savedStore);
		crewRepository.save(crew);

		CrewMember crewMember = CrewMemberObjectProvider.createCrewMember(savedUser1Id, crew, CrewMemberRole.MEMBER);
		crewMemberRepository.save(crewMember);

		crewId = crew.getId();
		crewMemberId = crewMember.getId();

	}

	@Test
	@DisplayName("[성공] 모임원 아이디로 모임원을 삭제한다")
	void delete_success() throws Exception {

		mockMvc.perform(delete("/api/v1/crews/{crewId}/crewMembers", crewId)
				.header(AUTHORIZATION, BEARER_TYPE + accessToken1)
				.accept(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(print())
			.andDo(document("crewMember-deleteById",
				resource(
					builder()
						.tag(CREW_MEMBER)
						.summary("모임원 아이디로 모임원 삭제 API")
						.description("모임원 아이디로 모임원을 삭제합니다.")
						.pathParameters(
							parameterWithName("crewId").description("모임 아이디"))
						.build()
				)
			));
	}
}