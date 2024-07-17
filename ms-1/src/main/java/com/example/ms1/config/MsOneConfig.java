package com.example.ms1.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;

@Configuration
public class MsOneConfig {
	private static final Logger LOGGER = LogManager.getLogger(MsOneConfig.class);

	@Bean
	public WebClient createWebClient(WebClient.Builder builder) {
		LOGGER.info("creating webclient in ms-1");
		return builder
			.baseUrl("http://ms-2:8082/api/ms2")
			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.defaultUriVariables(Collections.singletonMap("url", "http://ms-2:8082/api/ms2"))
			.build();
	}

}
