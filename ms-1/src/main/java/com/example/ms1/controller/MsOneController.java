package com.example.ms1.controller;

import com.example.ms1.dto.CustomDto;
import com.example.ms1.service.MsOneRequestService;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Context;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ms1")
public class MsOneController {
	private static final Logger LOGGER = LogManager.getLogger(MsOneController.class);

	private final MsOneRequestService msOneRequestService;

	public MsOneController(MsOneRequestService msOneRequestService) {
		this.msOneRequestService = msOneRequestService;
	}

	@GetMapping
	public ResponseEntity<String> getResponse() {
		LOGGER.info("Controller in ms-1");
		ResponseEntity<CustomDto> response = msOneRequestService.processRequest();

		return ResponseEntity.ok()
			.headers(getHttpHeadersWithTraceId())
			.body(response.getBody().message());
	}

	private HttpHeaders getHttpHeadersWithTraceId() {
		Context currentContext = Context.current();
		String traceId = Span.fromContext(currentContext).getSpanContext().getTraceId();

		HttpHeaders headers = new HttpHeaders();
		headers.add("X-Request-ID", traceId);
		return headers;
	}
}
