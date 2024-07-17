package com.example.ms3.service;

import com.example.ms3.dto.CustomDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class MsThreeRequestService {
	private static final Logger LOGGER = LogManager.getLogger(MsThreeRequestService.class);

	public CustomDto processRequest(CustomDto customDto) {
		LOGGER.info("processing request from ms-2");

		return new CustomDto(customDto.message() + " combined with ms-3");
	}
}