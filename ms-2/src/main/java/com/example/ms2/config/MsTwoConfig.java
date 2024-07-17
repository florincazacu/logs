package com.example.ms2.config;

import io.micrometer.core.instrument.MeterRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.cloud.sleuth.instrument.async.TraceableExecutorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.concurrent.ExecutorService;

@Configuration
public class MsTwoConfig {
	private static final Logger LOGGER = LogManager.getLogger(MsTwoConfig.class);

	@Bean
	public WebClient createWebClient(WebClient.Builder builder) {
		LOGGER.info("creating webclient in ms-2");
		return builder
			.baseUrl("http://ms-3:8083/api/ms3")
			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.defaultUriVariables(Collections.singletonMap("url", "http://ms-3:8083/api/ms3"))
			.build();
	}

	@Bean
	public ExecutorService createExecutorService(MeterRegistry meterRegistry, BeanFactory beanFactory) {
		int corePoolSize = 10;
		int maxPoolSize = 20;
		int queueCapacity = 100;

		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

		executor.setCorePoolSize(corePoolSize);
		executor.setMaxPoolSize(maxPoolSize);
		executor.setQueueCapacity(queueCapacity);
		executor.setThreadNamePrefix("MyExecutor-");
		executor.initialize();

		return new TraceableExecutorService(beanFactory, executor.getThreadPoolExecutor());
	}

}