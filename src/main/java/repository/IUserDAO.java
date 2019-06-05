package repository;

import java.util.List;

import repository.entity.UserEntity;

public interface IUserDAO {
	List<UserEntity> getUsers();
	UserEntity getUserByUserName(String userName);
	UserEntity getUserByUserId(Long userId);
	void insertUser(UserEntity user);
}
