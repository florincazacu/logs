package com.example.ms1.service;

import com.example.ms1.dto.CustomDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class MsOneRequestService {
	private static final Logger LOGGER = LogManager.getLogger(MsOneRequestService.class);

	private final WebClient webClient;

	public MsOneRequestService(WebClient webClient) {
		this.webClient = webClient;
	}

	public ResponseEntity<CustomDto> processRequest() {
		LOGGER.info("passing request to m-s2");

		WebClient.ResponseSpec response = webClient.post()
			.body(Mono.just(new CustomDto("Request from ms-1")), CustomDto.class)
			.retrieve();
		return response.toEntity(CustomDto.class).block();
	}

}
