package com.example.ms2.service;

import com.example.ms2.dto.CustomDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ExecutorService;

@Service
public class MsTwoRequestService {
	private static final Logger LOGGER = LogManager.getLogger(MsTwoRequestService.class);

	private final WebClient webClient;

	private final ExecutorService executorService;

	public MsTwoRequestService(WebClient webClient, ExecutorService executorService) {
		this.webClient = webClient;
		this.executorService = executorService;
	}

	public CustomDto processRequest(CustomDto customDto) {
		LOGGER.info("passing request to m-s2");

		try {
			WebClient.ResponseSpec retrieve = webClient.post()
				.body(Mono.just(new CustomDto("Request from ms-2")), CustomDto.class)
				.retrieve();

			ResponseEntity<CustomDto> response = retrieve.toEntity(CustomDto.class).block();

			LOGGER.info("got response from ms-3: {}", response.getBody());
		} catch (Exception e) {
			LOGGER.error("exception while sending request to ms-3: {}", e.getMessage());
		}

		Map<String, String> mdcContextMap = MDC.getCopyOfContextMap();

		try {
			executorService.submit(() -> {
				MDC.setContextMap(mdcContextMap);

				WebClient.ResponseSpec retrieve = webClient.post()
					.body(Mono.just(new CustomDto("Request from ms-2 on new thread")), CustomDto.class)
					.retrieve();

				ResponseEntity<CustomDto> response = retrieve.toEntity(CustomDto.class).block();

				LOGGER.info("got response from ms-3 on separate thread: {}", response.getBody());
			});
		} catch (Exception e) {
			LOGGER.info("exception while sending request to ms-3 on separate thread: {}", e.getMessage());
		}

		return new CustomDto(customDto.message() + " combined with message from ms-2");
	}
}