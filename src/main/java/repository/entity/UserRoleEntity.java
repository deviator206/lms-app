package repository.entity;

import java.util.List;

import model.UserRole;

public class UserRoleEntity {
	private Long userId;
	private List<UserRole> roles;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public List<UserRole> getRoles() {
		return roles;
	}
	public void setRoles(List<UserRole> roles) {
		this.roles = roles;
	}
	
}
