package com.prgrms.mukvengers.global.base.exception;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.CharEncoding;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.mukvengers.global.auth.token.exception.TokenException;
import com.prgrms.mukvengers.global.base.dto.ErrorResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {

	private final ObjectMapper objectMapper;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (TokenException e) {
			log.debug("[ExceptionHandler] token error message = {}", e.getMessage());
			generateErrorResponse(response, e);
		}
	}

	private void generateErrorResponse(HttpServletResponse response, TokenException e) throws IOException {
		response.setStatus(e.getErrorCode().getStatus().value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(CharEncoding.UTF_8);
		response.getWriter()
			.write(objectMapper.writeValueAsString(
				ErrorResponse.of(e.getErrorCode())));
	}

}
