package model;

public class JwtAuthenticationResponse {
	private String accessToken;
	private String tokenType = "Bearer";
	private UserInfo userInfo;
	
	

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public JwtAuthenticationResponse(String accessToken,UserInfo userInfo) {
		this.accessToken = accessToken;
		this.userInfo = userInfo;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
}
