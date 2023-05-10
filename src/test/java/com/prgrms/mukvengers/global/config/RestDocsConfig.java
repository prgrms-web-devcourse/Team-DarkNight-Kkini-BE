package com.prgrms.mukvengers.global.config;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;

@TestConfiguration
public class RestDocsConfig {

	@Bean
	public RestDocumentationResultHandler write() {
		return MockMvcRestDocumentationWrapper.document("{class-name}/{method-name}",
			preprocessRequest(prettyPrint()),
			preprocessResponse(prettyPrint()));
	}
}
