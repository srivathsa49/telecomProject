package com.telecomProject.CDR;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CdrApplication {

	public static void main(String[] args) {
		SpringApplication.run(CdrApplication.class, args);
	}

}
