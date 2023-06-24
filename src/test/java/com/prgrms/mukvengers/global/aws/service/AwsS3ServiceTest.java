package com.prgrms.mukvengers.global.aws.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.prgrms.mukvengers.base.SliceTest;
import com.prgrms.mukvengers.global.aws.dto.AwsS3;

class AwsS3ServiceTest extends SliceTest {

	public static final String SLASH = "/";

	private final String dirName = "profile"; // 변경 필요
	private final String defaultPath = "https://kr.object.ncloudstorage.com";

	@Mock
	private AmazonS3 amazonS3;

	@InjectMocks
	private AwsS3Service awsS3Service;

	@Test
	@DisplayName("[성공] S3에 파일을 업로드할 수 있다.")
	void upload_success() throws IOException, URISyntaxException {
		// given
		MockMultipartFile multipartFile = new MockMultipartFile(
			"test.txt",
			UUID.randomUUID().toString().substring(0, 8) + "test.txt",
			MediaType.TEXT_PLAIN_VALUE,
			"test".getBytes(StandardCharsets.UTF_8)
		);

		given(amazonS3.getUrl(isNull(), any())).willReturn(new URI(defaultPath).toURL());
		given(amazonS3.putObject(any(PutObjectRequest.class))).willReturn(mock(PutObjectResult.class));

		// when
		AwsS3 result = awsS3Service.upload(multipartFile, dirName);

		//then 209026c0dfb776fe
		assertThat(result.getKey()).contains("test.txt");
		assertThat(result.getPath()).contains(defaultPath);
		verify(amazonS3).putObject(any(PutObjectRequest.class));
	}

	@Test
	@DisplayName("[성공] S3에 파일을 삭제할 수 있다.")
	void remove_success() {
		// given
		AwsS3 awsS3 = new AwsS3();
		String key = "test.txt";
		awsS3.setKey(key);

		given(amazonS3.doesObjectExist(isNull(), any())).willReturn(true);

		// when
		awsS3Service.remove(awsS3);

		// then
		verify(amazonS3).deleteObject(any(DeleteObjectRequest.class));
	}

}