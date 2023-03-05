package com.prgrms.mukvengers.global.file.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FileManager {

	public Optional<File> convertMultipartFileToFile(MultipartFile multipartFile) {
		File newFile = new File(multipartFile.getOriginalFilename());
		try {
			if (newFile.createNewFile()) {
				log.debug(newFile.getName() + " : 임시 파일을 생성했습니다");
				try (FileOutputStream fos = new FileOutputStream(newFile)) {
					fos.write(multipartFile.getBytes());
				}
				return Optional.of(newFile);
			}
		} catch (IOException e) {
			throw new RuntimeException();
		}
		return Optional.empty();
	}

	public void removeFile(File targetFile) {
		String fileName = targetFile.getName();
		if (targetFile.delete()) {
			log.debug(fileName + " : 임시 파일를 삭제했습니다");
		} else {
			log.debug(fileName + " : 임시 파일를 삭제하지 못했습니다.");
		}
	}
}
