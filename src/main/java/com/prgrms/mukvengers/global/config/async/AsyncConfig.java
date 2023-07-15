package com.prgrms.mukvengers.global.config.async;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import lombok.RequiredArgsConstructor;

@EnableAsync
@Configuration
@RequiredArgsConstructor
public class AsyncConfig implements AsyncConfigurer {

	private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

	@Override
	public Executor getAsyncExecutor() {
		return threadPoolTaskExecutor;
	}

}