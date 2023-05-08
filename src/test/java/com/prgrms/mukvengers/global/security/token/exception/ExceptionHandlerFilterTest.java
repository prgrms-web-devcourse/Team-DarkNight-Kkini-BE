package com.prgrms.mukvengers.global.security.token.exception;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.codec.CharEncoding;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.mukvengers.base.SliceTest;
import com.prgrms.mukvengers.global.common.dto.ErrorResponse;

class ExceptionHandlerFilterTest extends SliceTest {

	private final ObjectMapper objectMapper = new ObjectMapper();
	private final ExceptionHandlerFilter filter = new ExceptionHandlerFilter(objectMapper);

	private final MockHttpServletRequest request = new MockHttpServletRequest();
	private final MockHttpServletResponse response = new MockHttpServletResponse();

	@ParameterizedTest
	@ValueSource(strings = {"Expired", "Invalid"})
	void testDoFilterInternal(String exceptionType) throws ServletException, IOException {
		// Given
		TokenException tokenException = getTokenException(exceptionType);
		ErrorResponse expectedErrorResponse = ErrorResponse.of(tokenException.getErrorCode());
		MockFilterChain filterChain = getMockFilterChain(tokenException);

		// When
		filter.doFilterInternal(request, response, filterChain);

		// Then
		assertThat(response.getStatus()).isEqualTo(tokenException.getErrorCode().getStatus().value());
		assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8.toString());
		assertThat(response.getCharacterEncoding()).isEqualTo(CharEncoding.UTF_8);
		assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(expectedErrorResponse));
	}

	private MockFilterChain getMockFilterChain(TokenException tokenException) {
		return new MockFilterChain() {
			@Override
			public void doFilter(ServletRequest request, ServletResponse response) {
				throw tokenException;
			}
		};
	}

	private TokenException getTokenException(String exceptionType) {
		switch (exceptionType) {
			case "Expired" -> {
				return new ExpiredTokenException();
			}
			case "Invalid" -> {
				return new InvalidTokenException();
			}
		}
		return null;
	}
}
