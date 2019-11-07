package model;

import security.SqlSafeUtil;

public class FilterUserRes {
	private String userName;
	private String userDisplayName;
	private String email;
	private String businessUnit;

	public boolean isValidFilter() {
		if ((userName != null && !userName.isEmpty()) || (userDisplayName != null && !userDisplayName.isEmpty())
				|| (email != null && !email.isEmpty()) || (businessUnit != null && !businessUnit.isEmpty())) {
			return true;
		}

		return false;
	}

	public Boolean isSqlInjectionSafe() {
		if (SqlSafeUtil.isSqlInjectionSafe(this.getBusinessUnit()) && SqlSafeUtil.isSqlInjectionSafe(this.getEmail())
				&& SqlSafeUtil.isSqlInjectionSafe(this.getUserDisplayName())
				&& SqlSafeUtil.isSqlInjectionSafe(this.getUserName())) {
			return true;
		}
		return false;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
