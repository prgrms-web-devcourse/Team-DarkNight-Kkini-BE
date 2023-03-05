package com.prgrms.mukvengers.global.file.api;

import java.io.IOException;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.prgrms.mukvengers.global.file.service.AwsS3Service;
import com.prgrms.mukvengers.global.file.util.AwsS3;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/s3")
@RequiredArgsConstructor
public class AwsS3Controller {

	private final AwsS3Service awsS3Service;

	@PostMapping("/resource")
	public AwsS3 upload(@RequestPart("file") MultipartFile multipartFile) throws IOException {
		return awsS3Service.upload(multipartFile, "upload");
	}

	@DeleteMapping("/resource")
	public void remove(AwsS3 awsS3) {
		awsS3Service.remove(awsS3);
	}
}