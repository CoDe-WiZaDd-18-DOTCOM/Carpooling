package com.example.carpooling;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CarpoolingApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure().load();
		System.setProperty("MONGO_URI", dotenv.get("MONGO_URI"));
		System.setProperty("SECRET_KEY", dotenv.get("SECRET_KEY"));
		System.setProperty("EMAIL",dotenv.get("EMAIL"));
		System.setProperty("PASSWORD",dotenv.get("PASSWORD"));
		SpringApplication.run(CarpoolingApplication.class, args);
	}

}
