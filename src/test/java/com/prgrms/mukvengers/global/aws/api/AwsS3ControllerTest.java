package com.prgrms.mukvengers.global.aws.api;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.*;
import static com.epages.restdocs.apispec.ResourceDocumentation.*;
import static com.epages.restdocs.apispec.ResourceSnippetParameters.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;

import com.epages.restdocs.apispec.Schema;
import com.prgrms.mukvengers.base.ControllerTest;
import com.prgrms.mukvengers.global.aws.dto.AwsS3;
import com.prgrms.mukvengers.global.aws.service.AwsS3Service;

class AwsS3ControllerTest extends ControllerTest {

	private static final Schema UPLOAD_RESPONSE = new Schema("upload");
	private static final String AWS = "AWS S3 API";

	@MockBean
	private AwsS3Service awsS3Service;

	@Test
	@DisplayName("[성공] S3에 파일을 업로드할 수 있다.")
	void upload_success_to_document() throws Exception {

		// given
		MockMultipartFile file = new MockMultipartFile( // MockMultipartFile은 스프링 테스트 모듈에서 제공함
			"file",
			"test.txt",
			MediaType.TEXT_PLAIN_VALUE,
			"test".getBytes(StandardCharsets.UTF_8)
		);

		AwsS3 awsS3 = new AwsS3();
		awsS3.setKey("test");
		awsS3.setPath("https://s3-ap-northeast-2.amazonaws.com/test/test.txt");

		given(awsS3Service.upload(any(), any())).willReturn(awsS3);

		// when
		mockMvc.perform(
				multipart("/api/v1/s3/resource")
					.file(file)
					.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
					.header(AUTHORIZATION, BEARER_TYPE + accessToken1))
			.andExpect(status().isOk())
			.andDo(print())
			.andDo(document("upload",
				resource(
					builder()
						.tags(AWS)
						.summary("파일 업로드 API")
						.responseSchema(UPLOAD_RESPONSE)
						.description("파일을 업로드 합니다.")
						.responseFields(
							fieldWithPath("key").type(STRING).description("s3 파일명"),
							fieldWithPath("path").type(STRING).description("s3 파일 경로"))
						.build()
				)
			));
	}

	@Test
	@DisplayName("[성공] S3에 파일 업로드 테스트")
	void upload_success() throws Exception {

		// given
		MockMultipartFile file = new MockMultipartFile(
			"file",
			"test.txt",
			MediaType.TEXT_PLAIN_VALUE,
			"test".getBytes(StandardCharsets.UTF_8)
		);

		AwsS3 awsS3 = new AwsS3();
		awsS3.setKey("test");
		awsS3.setPath("https://s3-ap-northeast-2.amazonaws.com/test/test.txt");

		given(awsS3Service.upload(any(), any())).willReturn(awsS3);

		// when
		mockMvc.perform(
				multipart("/api/v1/s3/resource")
					.file(file)
					.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
					.characterEncoding("UTF-8")
					.header(AUTHORIZATION, BEARER_TYPE + accessToken1))
			.andExpect(status().isOk())
			.andExpect(jsonPath("key").exists())
			.andExpect(jsonPath("path").exists())
			.andDo(print())
			.andDo(MockMvcRestDocumentation.document("uploadFile",
				requestParts(
					partWithName("file").description("파일")
				),
				responseFields(
					fieldWithPath("key").type(STRING).description("s3 파일명"),
					fieldWithPath("path").type(STRING).description("s3 파일 경로"))
			));

	}

	@Test
	@DisplayName("[성공] S3 파일을 삭제할 수 있다.")
	void remove_success() throws Exception {

		// given
		AwsS3 awsS3 = new AwsS3();
		awsS3.setKey("test");
		awsS3.setPath("https://s3-ap-northeast-2.amazonaws.com/test/test.txt");

		// when
		mockMvc.perform(delete("/api/v1/s3/resource")
				.header(AUTHORIZATION, BEARER_TYPE + accessToken1)
				.param("key", "test")
				.param("path", "https://s3-ap-northeast-2.amazonaws.com/test/test.txt")
				.characterEncoding("UTF-8"))
			.andDo(print());
	}

}