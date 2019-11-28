package repository;

import java.util.List;

import model.FilterUserRes;
import model.User;
import repository.entity.UserEntity;

public interface IUserDAO {
	List<UserEntity> getUsers();

	List<UserEntity> filterUsers(FilterUserRes filterUserRes);

	UserEntity getUserByUserName(String userName);

	UserEntity getUserByUserId(Long userId);
	public void deleteUserByUserId(Long userId);

	Long insertUser(UserEntity user);

	void updateUser(UserEntity user);
	
	public void disableUser(UserEntity user);
	public List<UserEntity> getUserDetailsByBuAndRole(String businessUnit, String role);
	public List<UserEntity> getUserDetailsByBu(String businessUnit);
	public void changePassword(UserEntity user);
}
