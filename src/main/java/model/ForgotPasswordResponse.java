package model;

public class ForgotPasswordResponse {
	private String email;
	private String userName;
	private String userDisplayName;
	private String forgotPasswordUri;
	private Boolean validUser;

	public Boolean isValidUser() {
		return validUser;
	}

	public void setValidUser(Boolean validUser) {
		this.validUser = validUser;
	}

	public String getForgotPasswordUri() {
		return forgotPasswordUri;
	}

	public void setForgotPasswordUri(String forgotPasswordUri) {
		this.forgotPasswordUri = forgotPasswordUri;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserDisplayName() {
		return userDisplayName;
	}

	public void setUserDisplayName(String userDisplayName) {
		this.userDisplayName = userDisplayName;
	}
}
