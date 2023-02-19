package com.prgrms.mukvengers.global.config.message;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;

import com.prgrms.mukvengers.global.utils.MessageUtil;

@Configuration
public class MessageConfig {

	@Bean
	public MessageSourceAccessor messageSourceAccessor(MessageSource messageSource) {
		MessageSourceAccessor messageSourceAccessor = new MessageSourceAccessor(messageSource);
		MessageUtil.setMessageSourceAccessor(messageSourceAccessor);
		return messageSourceAccessor;
	}
}