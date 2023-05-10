package com.prgrms.mukvengers.global.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.util.StringUtils;

import redis.embedded.RedisServer;

@TestConfiguration
public class EmbeddedRedisConfig {

	@Value("${spring.redis.port}")
	private int port;

	private RedisServer redisServer;

	@PostConstruct
	public void redisServer() throws IOException {
		redisServer = new RedisServer(isRedisRunning() ? findAvailablePort() : port);
		redisServer.start();
		System.out.println("Embedded Redis Start");
	}

	@PreDestroy
	public void stopRedis() {
		if (redisServer != null) {
			redisServer.stop();
			System.out.println("Embedded Redis Stop");
		}
	}

	private boolean isRedisRunning() throws IOException {
		return isRunning(executeGrepProcessCommand(port));
	}

	public int findAvailablePort() throws IOException {

		for (int port = 10000; port <= 65535; port++) {
			Process process = executeGrepProcessCommand(port);
			if (!isRunning(process)) {
				return port;
			}
		}

		throw new IllegalArgumentException("Not Found Available port: 10000 ~ 65535");
	}

	private Process executeGrepProcessCommand(int port) throws IOException {
		String command = String.format("netstat -nat | grep LISTEN|grep %d", port);
		String[] shell = {"/bin/sh", "-c", command};
		return Runtime.getRuntime().exec(shell);
	}

	private boolean isRunning(Process process) {
		String line;
		StringBuilder pidInfo = new StringBuilder();

		try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {

			while ((line = input.readLine()) != null) {
				pidInfo.append(line);
			}

		} catch (Exception e) {
		}

		return StringUtils.hasText(pidInfo.toString());
	}
}
