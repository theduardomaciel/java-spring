package dev.theduardomaciel.javaspring.model;

import java.util.UUID;

public class User {
	private final String id;
	private String username;
	private String password;
	
	public User(String username, String password) {
		this.id = UUID.randomUUID().toString();
		this.username = username;
		this.password = password;
	}
	
	public String getId() {
		return id;
	}
	
	public String getUsername() {
		return username;
	}
	
	@Override
	public String toString() {
		return "User{" +
				"username='" + username + '\'' +
				", password='" + password + '\'' +
				'}';
	}
}
