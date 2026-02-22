package com.example.autoskola;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AutoskolaApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutoskolaApplication.class, args);
	}

}
