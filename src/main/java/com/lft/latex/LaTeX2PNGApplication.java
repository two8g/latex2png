package com.lft.latex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by two8g on 16-3-4.
 */
@SpringBootApplication
@EnableScheduling
public class LaTeX2PNGApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(LaTeX2PNGApplication.class);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(LaTeX2PNGApplication.class, args);
	}
}
