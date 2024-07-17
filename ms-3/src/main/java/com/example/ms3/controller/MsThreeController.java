package com.example.ms3.controller;

import com.example.ms3.dto.CustomDto;
import com.example.ms3.service.MsThreeRequestService;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Context;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ms3")
public class MsThreeController {
	private static final Logger LOGGER = LogManager.getLogger(MsThreeController.class);

	private final MsThreeRequestService msThreeRequestService;

	public MsThreeController(MsThreeRequestService msThreeRequestService) {
		this.msThreeRequestService = msThreeRequestService;
	}

	@PostMapping
	public ResponseEntity<CustomDto> getResponse(@RequestBody CustomDto customDto) {
		LOGGER.info("Controller in ms-3. dto: {}", customDto);

		return ResponseEntity.ok()
			.headers(getHttpHeadersWithTraceId())
			.body(msThreeRequestService.processRequest(customDto));
	}

	private HttpHeaders getHttpHeadersWithTraceId() {
		Context currentContext = Context.current();
		String traceId = Span.fromContext(currentContext).getSpanContext().getTraceId();

		HttpHeaders headers = new HttpHeaders();
		headers.add("X-Request-ID", traceId);
		return headers;
	}
}