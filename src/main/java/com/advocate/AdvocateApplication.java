package com.advocate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AdvocateApplication {

	private static final Logger logger = LoggerFactory.getLogger(AdvocateApplication.class);

	public static void main(String[] args) {

		// Load environment variables from .env file
		// Dotenv dotenv = Dotenv.configure().load();
		// dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

		SpringApplication.run(AdvocateApplication.class, args);
		logger.info("Advocate Application Started....");
	}
}
