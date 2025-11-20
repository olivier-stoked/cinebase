package com.wiss.cinebase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // ← Diese Annotation macht die "Magie"
public class CinebaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(CinebaseApplication.class, args);
	}

}
