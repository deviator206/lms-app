package model;

import java.util.List;
import java.util.Map;

public class UserInfo {
	private Map<String, String> policies;
	private Map<String, String> policyActions;
	public Map<String, String> getPolicyActions() {
		return policyActions;
	}

	public void setPolicyActions(Map<String, String> policyActions) {
		this.policyActions = policyActions;
	}

	private Long userId;
	private String userName;

	private Boolean enabled;
	private Boolean tmpUser;

	public Boolean isTmpUser() {
		return tmpUser;
	}

	public void setTmpUser(Boolean tmpUser) {
		this.tmpUser = tmpUser;
	}

	public Boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	private String userDisplayName;
	private String businessUnit;
	private List<String> roles;

	public String getUserDisplayName() {
		return userDisplayName;
	}

	public void setUserDisplayName(String userDisplayName) {
		this.userDisplayName = userDisplayName;
	}

	public String getBusinessUnit() {
		return businessUnit;
	}

	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

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
