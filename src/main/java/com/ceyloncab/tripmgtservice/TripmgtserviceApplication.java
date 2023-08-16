package com.ceyloncab.tripmgtservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class TripmgtserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TripmgtserviceApplication.class, args);
	}

}
