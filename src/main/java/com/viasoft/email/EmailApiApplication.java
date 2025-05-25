package com.viasoft.email;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EmailApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmailApiApplication.class, args);
	}

	@PostConstruct
	public void started() {
		System.out.println("App iniciado (só pra Jacoco ver isso 😄)");
	}

}
