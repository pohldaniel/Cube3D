package de.cube3d.views;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;

public class PasswordForm {
	
	@NotEmpty(message = "required field!")
	@NotNull(message = "required field!")
	private String password;
	
	@NotEmpty(message = "required field!")
	@NotNull(message = "required field!")
	private String passwordConfirm;
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}	
}
