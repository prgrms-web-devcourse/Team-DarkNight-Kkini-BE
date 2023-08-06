package com.prgrms.mukvengers.global.infra.log;

import static java.nio.charset.StandardCharsets.*;
import static org.springframework.http.HttpMethod.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.AbstractRequestLoggingFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.WebUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class CustomRequestLoggingFilter extends AbstractRequestLoggingFilter {

	private static final int MAX_PAYLOAD_LENGTH = 200;
	private static final String MULTIPART_FORM_DATA = "multipart/form-data";
	private static final String X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";

	@Override
	protected boolean shouldLog(HttpServletRequest request) {
		return log.isDebugEnabled();
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		if (request.getMethod().equals(POST.name())) {
			setIncludePayload(true);
		}

		if (request.getMethod().equals(GET.name())) {
			setIncludeQueryString(true);
		}

		setIncludeClientInfo(true);
		setIncludeHeaders(true);
		super.doFilterInternal(request, response, filterChain);
	}

	@Override
	protected void beforeRequest(HttpServletRequest request, String message) {
		/* no-op */
	}

	@Override
	protected void afterRequest(HttpServletRequest request, String message) {
		log.debug("Request ---> {}", message);
	}

	@Override
	protected String getMessagePayload(HttpServletRequest request) {
		if (request.getContentType().equals(X_WWW_FORM_URLENCODED)) {
			return request.getParameterMap()
				.entrySet()
				.stream()
				.map(value -> value.getKey() + "=" + String.join("", value.getValue()))
				.collect(Collectors.joining("&"));
		}

		if (request.getContentType().equals(MULTIPART_FORM_DATA)) {
			return null;
		}

		ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);

		if (wrapper != null) {
			byte[] buf = wrapper.getContentAsByteArray();
			if (buf.length > 0) {
				return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buf), UTF_8)).lines()
					.collect(Collectors.joining());
			}
		}
		return null;
	}

	@Override
	public int getMaxPayloadLength() {
		return MAX_PAYLOAD_LENGTH;
	}

	@Override
	protected String createMessage(HttpServletRequest request, String prefix, String suffix) {
		StringBuilder msg = new StringBuilder();
		msg.append(prefix);
		msg.append(request.getMethod()).append(' ');
		msg.append(request.getRequestURI());

		if (isIncludeQueryString()) {
			appendQueryStringMessage(request, msg);
		}

		if (isIncludeClientInfo()) {
			appendClientInfoMessage(request, msg);
		}

		if (isIncludeHeaders()) {
			appendHeadersMessage(request, msg);
		}

		if (isIncludePayload()) {
			appendPayloadMessage(request, msg);
		}

		msg.append(suffix);
		return msg.toString();
	}

	private void appendQueryStringMessage(HttpServletRequest request, StringBuilder msg) {
		Enumeration<String> parameterNames = request.getParameterNames();
		Map<String, String[]> parameterMap = request.getParameterMap();
		if (parameterNames.hasMoreElements()) {
			msg.append('?');
		}
		while (parameterNames.hasMoreElements()) {
			String parameterName = parameterNames.nextElement();
			String[] parameterValues = parameterMap.get(parameterName);
			msg.append(parameterName).append("=");

			if (parameterValues.length == 1) {
				msg.append(parameterValues[0]);
			}
			if (parameterValues.length > 1) {
				msg.append(Arrays.toString(parameterValues));
			}
			msg.append("&");
		}
		int querySeparatorIndex = msg.lastIndexOf("&");
		if (querySeparatorIndex >= 0) {
			msg.deleteCharAt(querySeparatorIndex);
		}
	}

	private void appendHeadersMessage(HttpServletRequest request, StringBuilder msg) {
		HttpHeaders headers = new ServletServerHttpRequest(request).getHeaders();
		if (getHeaderPredicate() != null) {
			Enumeration<String> names = request.getHeaderNames();
			while (names.hasMoreElements()) {
				String header = names.nextElement();
				if (!getHeaderPredicate().test(header)) {
					headers.set(header, "masked");
				}
			}
		}
		msg.append(", headers=").append(headers);
	}

	private void appendClientInfoMessage(HttpServletRequest request, StringBuilder msg) {
		String client = request.getRemoteAddr();
		if (StringUtils.hasLength(client)) {
			msg.append(", client=").append(client);
		}
		HttpSession session = request.getSession(false);
		if (session != null) {
			msg.append(", session=").append(session.getId());
		}
		String user = request.getRemoteUser();
		if (user != null) {
			msg.append(", user=").append(user);
		}
	}

	private void appendPayloadMessage(HttpServletRequest request, StringBuilder msg) {
		String payload = getMessagePayload(request);
		if (payload != null) {
			msg.append(", payload=").append(payload);
		}
	}
}
