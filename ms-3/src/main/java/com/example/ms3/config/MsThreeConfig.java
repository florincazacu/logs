package com.example.ms3.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;

@Configuration
public class MsThreeConfig {
	private static final Logger LOGGER = LogManager.getLogger(MsThreeConfig.class);

	@Bean
	public WebClient createWebClient(WebClient.Builder builder) {
		LOGGER.info("creating webclient in ms-3");
		return builder
			.baseUrl("http://ms-4:8084/api/ms4")
			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.defaultUriVariables(Collections.singletonMap("url", "http://ms-4:8084/api/ms4"))
			.build();
	}

}
