package repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import repository.entity.UserEntity;

public class UserSummaryRowMapper implements RowMapper<UserEntity> {

	@Override
	public UserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		UserEntity user = new UserEntity();
		user.setId(rs.getLong("ID"));
		user.setUserName(rs.getString("USERNAME"));		
		user.setEmail(rs.getString("EMAIL"));
		user.setUserDisplayName(rs.getString("USER_DISPLAY_NAME"));
		user.setBusinessUnit(rs.getString("BU"));
		if(rs.getObject("ENABLED") != null) {
			user.setEnabled(rs.getBoolean("ENABLED"));
		}
		return user;
	}

}
