package service;

import java.util.List;
import java.util.Map;

import model.FilterUserRes;
import model.ForgotPasswordResponse;
import model.User;
import model.UserRegistrationDetails;
import model.UserRes;
import model.UserRoles;
import repository.entity.UserEntity;

public interface IUserService {
	public List<UserRes> getUsers();

	public Map<Long, UserRes> getCachedUsersSummaryMap();

	public User getUserByUserName(String userName);

	public User getUserByUserId(Long userId);

	public void updateUserByUserId(UserRegistrationDetails userRegistrationDetails);

	public void changePasswordByUserId(UserRegistrationDetails userRegistrationDetails);

	public void addUser(UserRegistrationDetails userRegistrationDetails);

	public void replaceRoles(UserRoles userRoles);

	public ForgotPasswordResponse forgotPassword(UserRegistrationDetails userRegistrationDetails);

	public List<UserRes> filterUsers(FilterUserRes filterUserRes);
	
	public void disableUser(Long userId);
}
