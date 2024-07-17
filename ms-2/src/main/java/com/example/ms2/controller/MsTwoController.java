package com.example.ms2.controller;

import com.example.ms2.dto.CustomDto;
import com.example.ms2.service.MsTwoRequestService;
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
@RequestMapping("/api/ms2")
public class MsTwoController {
	private static final Logger LOGGER = LogManager.getLogger(MsTwoController.class);

	private final MsTwoRequestService msTwoRequestService;

	public MsTwoController(MsTwoRequestService msTwoRequestService) {
		this.msTwoRequestService = msTwoRequestService;
	}

	@PostMapping
	public ResponseEntity<CustomDto> getResponse(@RequestBody CustomDto customDto) {
		LOGGER.info("Controller in ms-2");

		return ResponseEntity.ok()
			.headers(getHttpHeadersWithTraceId())
			.body(msTwoRequestService.processRequest(customDto));
	}

	private HttpHeaders getHttpHeadersWithTraceId() {
		Context currentContext = Context.current();
		String traceId = Span.fromContext(currentContext).getSpanContext().getTraceId();

		HttpHeaders headers = new HttpHeaders();
		headers.add("X-Request-ID", traceId);
		return headers;
	}
}