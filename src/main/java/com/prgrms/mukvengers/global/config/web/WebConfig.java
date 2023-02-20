package com.prgrms.mukvengers.global.config.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;


@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

	@Override
	protected void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOriginPatterns("http://localhost:8080/", "/**")
			.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE")
			.allowCredentials(true);
	}

	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/docs/**")
			.addResourceLocations("classpath:/static/docs/");
	}

}

