package mapper;

import model.RefDataRes;
import model.User;
import model.UserRes;
import repository.entity.RefDataEntity;

public class ModelMappers {
	public static UserRes mapUserToUserRes(User user, UserRes userRes) {
		userRes.setUserId(user.getId());
		userRes.setUserName(user.getUserName());
		userRes.setEmail(user.getEmail());
		userRes.setEnabled(user.isEnabled());
		return userRes;
	}

	public static RefDataRes mapRefEntityToRefRes(RefDataEntity refDataEntity, RefDataRes refDataRes) {
		refDataRes.setCode(refDataEntity.getCode());
		refDataRes.setName(refDataEntity.getName());
		refDataRes.setType(refDataEntity.getType());
		return refDataRes;
	}

	public static RefDataEntity mapRefResToRefEntity(RefDataRes refDataRes, RefDataEntity refDataEntity) {
		refDataEntity.setCode(refDataRes.getCode());
		refDataEntity.setName(refDataRes.getName());
		refDataEntity.setType(refDataRes.getType());
		return refDataEntity;
	}
}
