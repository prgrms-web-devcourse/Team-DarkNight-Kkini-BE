package com.prgrms.mukvengers.global.config.auth;

import static org.springframework.http.HttpMethod.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.SecurityFilterChain;

import com.prgrms.mukvengers.global.auth.oauth.handler.OAuthAuthenticationFailureHandler;
import com.prgrms.mukvengers.global.auth.oauth.handler.OAuthAuthenticationSuccessHandler;
import com.prgrms.mukvengers.global.auth.oauth.repository.HttpCookieOAuthAuthorizationRequestRepository;
import com.prgrms.mukvengers.global.auth.token.filter.JwtAuthenticationEntryPoint;
import com.prgrms.mukvengers.global.auth.token.filter.JwtAuthenticationFilter;
import com.prgrms.mukvengers.global.base.exception.ExceptionHandlerFilter;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@Configuration
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
			.antMatchers(OPTIONS, "/ws").permitAll()
			.antMatchers(OPTIONS, "/ws/*").permitAll()
			.antMatchers("**/info").permitAll()
			.antMatchers("/app/**").permitAll()
			.antMatchers("/topic/**").permitAll()
			.antMatchers("/docs/**").permitAll()
			.antMatchers("/oauth2/**").permitAll()
			.antMatchers("/favicon.ico").permitAll()
			.antMatchers("/api/v1/sample").permitAll()
			.antMatchers("/api/v1/stores/**").permitAll()
			.antMatchers("/api/v1/tokens").permitAll()
			.antMatchers("/ws/**").permitAll()
			.antMatchers("/ws/*").permitAll()
			.antMatchers("/chat*").permitAll()
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

