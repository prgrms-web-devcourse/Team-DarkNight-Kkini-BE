package com.prgrms.mukvengers.base;

import static com.prgrms.mukvengers.utils.UserObjectProvider.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.mukvengers.config.RestDocsConfig;
import com.prgrms.mukvengers.domain.user.model.User;
import com.prgrms.mukvengers.domain.user.repository.UserRepository;
import com.prgrms.mukvengers.global.security.jwt.JwtTokenProvider;

@Transactional
@SpringBootTest
@Import({RestDocsConfig.class})
@ExtendWith(RestDocumentationExtension.class)
public abstract class ControllerTest {

	protected final String BEARER_TYPE = "Bearer ";

	@Autowired
	protected RestDocumentationResultHandler restDocs;

	@Autowired
	protected JwtTokenProvider jwtTokenProvider;

	@Autowired
	protected UserRepository userRepository;

	@Autowired
	protected ObjectMapper objectMapper;

	protected MockMvc mockMvc;

	protected User savedUser;
	protected Long savedUserId;
	protected String ACCESS_TOKEN;

	@BeforeEach
	void setUpRestDocs(WebApplicationContext webApplicationContext,
		RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
			.addFilter(new CharacterEncodingFilter("UTF-8", true))
			.apply(documentationConfiguration(restDocumentation))
			.apply(springSecurity())
			.alwaysDo(restDocs)
			.build();
	}

	@BeforeEach
	void setUpLogin() {
		savedUser = userRepository.save(createUser());
		savedUserId = savedUser.getId();
		ACCESS_TOKEN = jwtTokenProvider.createAccessToken(savedUserId, "USER");
	}

}
