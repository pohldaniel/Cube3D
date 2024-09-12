package de.security.view;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;

public class LoginForm {
	
	@NotEmpty(message = "required field!")
	@NotNull(message = "required field!")
	private String username;
	
	@NotEmpty(message = "required field!")
	@NotNull(message = "required field!")
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}	
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
