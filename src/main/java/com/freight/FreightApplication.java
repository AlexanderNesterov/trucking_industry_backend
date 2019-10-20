package com.freight;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("application.properties")
public class FreightApplication {
	public static void main(String[] args) {
		SpringApplication.run(FreightApplication.class, args);
	}
}
