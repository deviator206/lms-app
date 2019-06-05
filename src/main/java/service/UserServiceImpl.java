package service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import model.User;
import model.UserRegistrationDetails;
import model.UserRole;
import repository.IUserDAO;
import repository.entity.UserEntity;
import repository.entity.UserRoleEntity;
import security.UserPrincipal;

@Service
public class UserServiceImpl implements IUserService {
	@Autowired
	private IUserDAO userDAO;

	@Resource
	private PasswordEncoder passwordEncoder;

	public List<User> getUsers() {
		List<UserEntity> userEntities = userDAO.getUsers();
		List<User> users = new ArrayList<User>();
		for (UserEntity userEntity : userEntities) {
			User user = new User();
			this.mapUserEntityToUser(userEntity, user);
			UserRoleEntity userRoleEntity = this.getUserRoleByUserId(userEntity.getId());
			user.setRoles(userRoleEntity.getRoles());
			users.add(user);
		}
		return users;
	}

	@Override
	public void addUser(UserRegistrationDetails userRegistrationDetails) {
		UserEntity user = new UserEntity();
		user.setUserName(userRegistrationDetails.getUserName());
		user.setEnabled(true);
		user.setEmail(userRegistrationDetails.getEmail());
		user.setPassword(passwordEncoder.encode(userRegistrationDetails.getPassword()));
		userDAO.insertUser(user);
	}

	@Override
	public User getUserByUserName(String userName) {
		UserEntity userEntity = userDAO.getUserByUserName(userName);
		//System.out.println("Password --------------------- >>> "+ userEntity.getPassword());
		UserRoleEntity userRoleEntity = this.getUserRoleByUserId(userEntity.getId());
		User user = new User();
		this.mapUserEntityToUser(userEntity, user);
		user.setRoles(userRoleEntity.getRoles());
		return user;
	}

	public User mapUserEntityToUser(UserEntity userEntity, User user) {
		user.setId(userEntity.getId());
		user.setUserName(userEntity.getUserName());
		user.setEmail(userEntity.getEmail());
		user.setPassword(userEntity.getPassword());
		user.setEnabled(userEntity.isEnabled());
		return user;
	}

	// Implement with DAO
	public UserRoleEntity getUserRoleByUserId(Long userId) {
		List<UserRole> roles = new ArrayList<UserRole>();
		roles.add(UserRole.ADMIN);
		UserRoleEntity userRoleEntity = new UserRoleEntity();
		userRoleEntity.setUserId(userId);
		userRoleEntity.setRoles(roles);
		return userRoleEntity;
	}

	@Override
	public User getUserByUserId(Long userId) {
		UserEntity userEntity = userDAO.getUserByUserId(userId);
		UserRoleEntity userRoleEntity = this.getUserRoleByUserId(userId);
		User user = new User();
		this.mapUserEntityToUser(userEntity, user);
		user.setRoles(userRoleEntity.getRoles());
		return user;
	}

	/*
	 * public List<UserRole> getUserRolesByUserName(String userName) {
	 * List<UserRole> roles = new ArrayList<UserRole>(); roles.add("ADMIN"); return
	 * roles; }
	 */

}
