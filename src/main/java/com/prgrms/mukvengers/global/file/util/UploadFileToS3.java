package com.prgrms.mukvengers.global.file.util;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UploadFileToS3 {

	private final AmazonS3Client amazonS3Client;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	public String upload(File uploadFile, String dirName, String fileName) {
		String savedFileName = dirName + "/" + fileName;
		amazonS3Client.putObject(new PutObjectRequest(bucket, savedFileName, uploadFile));
		return amazonS3Client.getUrl(bucket, savedFileName).toString();
	}
}
