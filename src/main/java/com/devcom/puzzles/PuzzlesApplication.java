package com.devcom.puzzles;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
public class PuzzlesApplication {

	public static void main(String[] args) {
		SpringApplication.run(PuzzlesApplication.class, args);
	}

}
