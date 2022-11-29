package com.york;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.springframework.boot.SpringApplication.run;

@EnableBatchProcessing
@SpringBootApplication
public class YorkApplication {

	public static void main(String[] args) {
		run(YorkApplication.class, args);
	}

}
