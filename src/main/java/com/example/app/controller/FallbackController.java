package com.example.app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
public class FallbackController {
	
	@GetMapping("/fallback/api/products")
	public Mono<String> productServiceFallback() {
		return Mono.just("Service unavailable");
	}
}

