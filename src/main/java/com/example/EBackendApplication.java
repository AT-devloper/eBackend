package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//(exclude = {SecurityAutoConfiguration.class})

public class EBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EBackendApplication.class, args);
	}

}
