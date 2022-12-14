package com.otica.oticaapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableFeignClients
@SpringBootApplication
public class OticaApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(OticaApiApplication.class, args);
	}
}
