package com.prgrms.mukvengers.global.security.token.filter;

import static org.springframework.http.HttpStatus.*;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.mukvengers.global.common.dto.ErrorResponse;
import com.prgrms.mukvengers.global.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private static final String ERROR_LOG_MESSAGE = "[ERROR] {} : {}";

	private final ObjectMapper objectMapper;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException {
		log.info(ERROR_LOG_MESSAGE, authException.getClass().getSimpleName(), authException.getMessage());
		response.setStatus(UNAUTHORIZED.value());
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter()
			.write(objectMapper.writeValueAsString(
				ErrorResponse.of(ErrorCode.HANDLE_ACCESS_DENIED))
			);
	}
}
