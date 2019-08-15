package repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import repository.entity.UserRoleEntity;

public class UserRoleRowMapper implements RowMapper<UserRoleEntity> {

	@Override
	public UserRoleEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		UserRoleEntity userRoleEntity = new UserRoleEntity();
		userRoleEntity.setUserId(rs.getLong("USER_ID"));
		userRoleEntity.setRole(rs.getString("USER_ROLE"));
		return userRoleEntity;
	}

}
