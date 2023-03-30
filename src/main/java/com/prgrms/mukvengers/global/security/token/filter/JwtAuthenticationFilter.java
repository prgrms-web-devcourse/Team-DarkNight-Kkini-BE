package com.prgrms.mukvengers.global.security.token.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.prgrms.mukvengers.global.security.token.dto.jwt.JwtAuthentication;
import com.prgrms.mukvengers.global.security.token.dto.jwt.JwtAuthenticationToken;
import com.prgrms.mukvengers.global.security.token.service.JwtTokenProvider;
import com.prgrms.mukvengers.global.utils.ExtractUtil;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		String accessToken = getAccessToken(request);
		if (accessToken != null) {
			JwtAuthenticationToken authentication = createAuthentication(accessToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		filterChain.doFilter(request, response);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return request.getRequestURI().endsWith("tokens")
			&& request.getMethod().equalsIgnoreCase("POST");
	}

	private String getAccessToken(HttpServletRequest request) {
		String token = ExtractUtil.extractTokenFromRequest(request);
		if (token != null) {
			jwtTokenProvider.validateToken(token);
		}
		return token;
	}

	private JwtAuthenticationToken createAuthentication(String accessToken) {
		Claims claims = jwtTokenProvider.getClaims(accessToken);

		Long userId = claims.get("userId", Long.class);
		String role = claims.get("role", String.class);

		JwtAuthentication principal = new JwtAuthentication(userId, accessToken);

		return new JwtAuthenticationToken(principal, null, toAuthorities(role));
	}

	private List<GrantedAuthority> toAuthorities(String role) {
		return List.of(new SimpleGrantedAuthority(role));
	}

}
