package repository;

import java.util.List;

import repository.entity.UserRoleEntity;

public interface IUserRoleDAO {
	List<UserRoleEntity> getUserRolesByUserId(Long userId);

	void insertUserRole(Long userId,String role);
	
	void deleteUserRole(Long userId);
	
	void replaceRoles(Long userId,List<String> roles);
}
