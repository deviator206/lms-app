package repository;

import java.util.List;

import repository.entity.UserRoleEntity;

public interface IUserRoleDAO {
	List<UserRoleEntity> getUserRolesByUserName();
}
