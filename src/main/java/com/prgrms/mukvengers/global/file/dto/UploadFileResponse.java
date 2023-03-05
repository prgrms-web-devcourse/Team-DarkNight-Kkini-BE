package com.prgrms.mukvengers.global.file.dto;

public record UploadFileResponse(
	String originalFileName,
	String uploadFilePath) {

	public static UploadFileResponse toUploadFileResponse(String originalFileName, String uploadFilePath) {
		return new UploadFileResponse(originalFileName, uploadFilePath);
	}
}
