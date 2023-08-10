package com.prgrms.mukvengers;

import java.util.Locale;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MukvengersApplication {

	public static void main(String[] args) {
		SpringApplication.run(MukvengersApplication.class, args);
	}

	@PostConstruct
	void setTimeAndLocale() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
		Locale.setDefault(Locale.KOREA);
	}
}
