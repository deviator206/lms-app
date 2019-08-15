package model;

import java.util.Map;

public class UserInfo {
	private Map<String, String> policies;
	private Long userId;
	private String userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Map<String, String> getPolicies() {
		return policies;
	}

	public void setPolicies(Map<String, String> policies) {
		this.policies = policies;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
