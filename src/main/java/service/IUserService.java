package service;

import java.util.List;

import model.ForgotPasswordResponse;
import model.User;
import model.UserRegistrationDetails;
import model.UserRoles;

public interface IUserService {
	public List<User> getUsers();

	public User getUserByUserName(String userName);

	public User getUserByUserId(Long userId);

	public void addUser(UserRegistrationDetails userRegistrationDetails);

	public void replaceRoles(UserRoles userRoles);

	public ForgotPasswordResponse forgotPassword(UserRegistrationDetails userRegistrationDetails);
}
