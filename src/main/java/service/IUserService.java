package service;

import java.util.List;

import model.User;
import model.UserRegistrationDetails;

public interface IUserService {
	public List<User> getUsers();
	public User getUserByUserName(String userName);
	public User getUserByUserId(Long userId);
	public void addUser(UserRegistrationDetails userRegistrationDetails);
}
