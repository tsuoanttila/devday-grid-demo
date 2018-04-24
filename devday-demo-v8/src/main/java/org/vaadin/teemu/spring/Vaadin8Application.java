package org.vaadin.teemu.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * The entry point of the Spring Boot application.
 */
@SpringBootApplication
public class Vaadin8Application extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(Vaadin8Application.class, args);
	}
}
