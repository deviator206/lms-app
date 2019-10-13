package repository;

import java.util.List;

import model.FilterUserRes;
import repository.entity.UserEntity;

public interface IUserDAO {
	List<UserEntity> getUsers();

	List<UserEntity> filterUsers(FilterUserRes filterUserRes);

	UserEntity getUserByUserName(String userName);

	UserEntity getUserByUserId(Long userId);

	Long insertUser(UserEntity user);

	void updateUser(UserEntity user);
	
	public void disableUser(UserEntity user);
	public List<UserEntity> getUserDetailsByBuAndRole(String businessUnit, String role);
}
