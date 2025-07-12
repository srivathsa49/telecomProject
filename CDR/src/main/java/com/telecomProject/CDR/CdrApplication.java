package com.telecomProject.CDR;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableKafka
@EnableCaching
@EnableFeignClients
@EnableScheduling
public class CdrApplication {

	public static void main(String[] args) {
		SpringApplication.run(CdrApplication.class, args);
	}

}
