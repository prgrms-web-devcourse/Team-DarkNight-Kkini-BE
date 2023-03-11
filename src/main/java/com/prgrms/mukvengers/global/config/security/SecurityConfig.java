package com.prgrms.mukvengers.global.config.security;

import static org.springframework.http.HttpMethod.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.SecurityFilterChain;

import com.prgrms.mukvengers.global.security.jwt.JwtAuthenticationEntryPoint;
import com.prgrms.mukvengers.global.security.jwt.JwtAuthenticationFilter;
import com.prgrms.mukvengers.global.security.oauth.handler.HttpCookieOAuthAuthorizationRequestRepository;
import com.prgrms.mukvengers.global.security.oauth.handler.OAuthAuthenticationFailureHandler;
import com.prgrms.mukvengers.global.security.oauth.handler.OAuthAuthenticationSuccessHandler;
import com.prgrms.mukvengers.global.security.token.exception.ExceptionHandlerFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final OAuthAuthenticationSuccessHandler oauthAuthenticationSuccessHandler;
	private final OAuthAuthenticationFailureHandler oauthAuthenticationFailureHandler;
	private final HttpCookieOAuthAuthorizationRequestRepository httpCookieOAuthAuthorizationRequestRepository;
	private final ExceptionHandlerFilter exceptionHandlerFilter;

	@Bean
	public SecurityFilterChain httpSecurity(HttpSecurity http) throws Exception {
		return http
			.cors()
			.and()
			.authorizeHttpRequests()
			.antMatchers(OPTIONS, "/api/*").permitAll()
			.antMatchers("/docs/**").permitAll()
			.antMatchers("/oauth2/**").permitAll()
			.antMatchers("/favicon.ico").permitAll()
			.antMatchers("/api/v1/sample").permitAll()
			.antMatchers("/api/v1/stores/**").permitAll()
			.antMatchers("/api/v1/tokens").permitAll()
			.antMatchers("/ws/**").permitAll()
			.antMatchers("/chat/**").permitAll()
			.antMatchers(GET, "/api/v1/crews").permitAll()
			.antMatchers("/").permitAll()
			.anyRequest().authenticated()
			.and()
			.httpBasic().disable()
			.rememberMe().disable()
			.csrf().disable()
			.logout().disable()
			.requestCache().disable()
			.formLogin().disable()
			.headers().disable()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.oauth2Login()
			.authorizationEndpoint().baseUri("/oauth2/authorization")
			.authorizationRequestRepository(httpCookieOAuthAuthorizationRequestRepository)
			.and()
			.successHandler(oauthAuthenticationSuccessHandler)
			.failureHandler(oauthAuthenticationFailureHandler)
			.and()
			.exceptionHandling()
			.authenticationEntryPoint(jwtAuthenticationEntryPoint)
			.and()
			.addFilterBefore(jwtAuthenticationFilter, OAuth2AuthorizationRequestRedirectFilter.class)
			.addFilterBefore(exceptionHandlerFilter, JwtAuthenticationFilter.class)
			.build();
	}
}

