package service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import model.User;
import model.UserRegistrationDetails;
import model.UserRoles;
import repository.IUserDAO;
import repository.IUserRoleDAO;
import repository.entity.UserEntity;
import repository.entity.UserRoleEntity;

@Service
public class UserServiceImpl implements IUserService {
	@Autowired
	private IUserDAO userDAO;

	@Autowired
	private IUserRoleDAO userRoleDAO;

	@Resource
	private PasswordEncoder passwordEncoder;

	public List<User> getUsers() {
		List<UserEntity> userEntities = userDAO.getUsers();
		List<User> users = new ArrayList<User>();
		for (UserEntity userEntity : userEntities) {
			User user = new User();
			this.mapUserEntityToUser(userEntity, user);
			List<String> roles = this.getUserRoleByUserId(userEntity.getId());
			user.setRoles(roles);
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
	public User getUserByUserId(Long userId) {
		UserEntity userEntity = userDAO.getUserByUserId(userId);
		// System.out.println("Password --------------------- >>> "+
		// userEntity.getPassword());
		List<String> roles = this.getUserRoleByUserId(userEntity.getId());
		User user = new User();
		this.mapUserEntityToUser(userEntity, user);
		user.setRoles(roles);
		return user;
	}

	@Override
	public User getUserByUserName(String userName) {
		UserEntity userEntity = userDAO.getUserByUserName(userName);
		// System.out.println("Password --------------------- >>> "+
		// userEntity.getPassword());
		List<String> roles = this.getUserRoleByUserId(userEntity.getId());
		User user = new User();
		this.mapUserEntityToUser(userEntity, user);
		user.setRoles(roles);
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
	public List<String> getUserRoleByUserId(Long userId) {
		List<UserRoleEntity> roleEntities = userRoleDAO.getUserRolesByUserId(userId);
		List<String> roles = new ArrayList<String>();
		if (roleEntities != null) {
			for (UserRoleEntity role : roleEntities) {
				roles.add(role.getRole());
			}
		}

		// TODO : Remove this
		roles.add("ADMIN");

		return roles;
	}

	@Override
	public void replaceRoles(UserRoles userRoles) {
		userRoleDAO.replaceRoles(userRoles.getUserId(),userRoles.getRoles());
		
	}

	/*
	 * public List<UserRole> getUserRolesByUserName(String userName) {
	 * List<UserRole> roles = new ArrayList<UserRole>(); roles.add("ADMIN"); return
	 * roles; }
	 */

}
