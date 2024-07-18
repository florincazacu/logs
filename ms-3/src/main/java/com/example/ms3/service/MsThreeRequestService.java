package com.example.ms3.service;

import com.example.ms3.dto.CustomDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class MsThreeRequestService {
	private static final Logger LOGGER = LogManager.getLogger(MsThreeRequestService.class);

	private final WebClient webClient;

	public MsThreeRequestService(WebClient webClient) {
		this.webClient = webClient;
	}

	public CustomDto processRequest(CustomDto customDto) {
		LOGGER.info("processing request from ms-2");

		WebClient.ResponseSpec response = webClient.get()
			.retrieve();

		String body = response.toEntity(String.class).block().getBody();


		return new CustomDto(customDto.message() + " combined with ms-3 " + "response from ms-4 " + body);
	}
}