package com.swyg.findingahomesafely;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class FindingAHomeSafelyApplication {

	public static void main(String[] args) {
		SpringApplication.run(FindingAHomeSafelyApplication.class, args);
	}

}
