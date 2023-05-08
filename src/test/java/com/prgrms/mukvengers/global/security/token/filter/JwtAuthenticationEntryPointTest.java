package com.prgrms.mukvengers.global.security.token.filter;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.CharEncoding;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.mukvengers.base.SliceTest;
import com.prgrms.mukvengers.global.common.dto.ErrorResponse;
import com.prgrms.mukvengers.global.exception.ErrorCode;

class JwtAuthenticationEntryPointTest extends SliceTest {

	private final ObjectMapper objectMapper = new ObjectMapper();
	private final JwtAuthenticationEntryPoint entryPoint = new JwtAuthenticationEntryPoint(objectMapper);

	private final MockHttpServletRequest request = new MockHttpServletRequest();
	private final MockHttpServletResponse response = new MockHttpServletResponse();

	@Test
	void testCommence() throws IOException {
		// Given
		ErrorResponse expectedErrorResponse = ErrorResponse.of(ErrorCode.HANDLE_ACCESS_DENIED);
		AuthenticationException authException = new BadCredentialsException("Bad Credentials");

		// When
		entryPoint.commence(request, response, authException);

		// Then
		assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
		assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8.toString());
		assertThat(response.getCharacterEncoding()).isEqualTo(CharEncoding.UTF_8);
		assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(expectedErrorResponse));
	}
}
