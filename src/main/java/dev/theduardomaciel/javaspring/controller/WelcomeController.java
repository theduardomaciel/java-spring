package dev.theduardomaciel.javaspring.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class WelcomeController {
	@GetMapping
	public String welcome(){
		return "Welcome to a Spring Boot REST API";
	}
}