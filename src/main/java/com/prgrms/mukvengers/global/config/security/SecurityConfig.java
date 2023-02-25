package com.prgrms.mukvengers.global.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.SecurityFilterChain;

import com.prgrms.mukvengers.global.security.jwt.JwtAuthenticationEntryPoint;
import com.prgrms.mukvengers.global.security.jwt.JwtAuthenticationFilter;
import com.prgrms.mukvengers.global.security.token.exception.ExceptionHandlerFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final ExceptionHandlerFilter exceptionHandlerFilter;

	@Bean
	public SecurityFilterChain httpSecurity(HttpSecurity http) throws Exception {
		return http
			.cors()
			.and()
			.authorizeHttpRequests()
			.antMatchers("/tokens").permitAll()
			.antMatchers("/docs/**").permitAll()
			.antMatchers("/favicon.ico").permitAll()
			.antMatchers("/api/v1/sample").permitAll()
			.antMatchers("/api/v1/stores/**").permitAll()
			.antMatchers("/api/v1/crews/**").permitAll()
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
			.exceptionHandling()
			.authenticationEntryPoint(jwtAuthenticationEntryPoint)
			.and()
			.addFilterBefore(jwtAuthenticationFilter, OAuth2AuthorizationRequestRedirectFilter.class)
			.addFilterBefore(exceptionHandlerFilter, JwtAuthenticationFilter.class)
			.build();
	}

}

