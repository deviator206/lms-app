package mapper;

import model.User;
import model.UserRes;

public class ModelMappers {
	public static UserRes mapUserToUserRes(User user, UserRes userRes) {
		userRes.setId(user.getId());
		userRes.setUserName(user.getUserName());
		userRes.setEmail(user.getEmail());
		userRes.setEnabled(user.isEnabled());
		return userRes;
	}
}
