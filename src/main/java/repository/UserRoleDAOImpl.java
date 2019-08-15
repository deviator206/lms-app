package repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import repository.entity.UserRoleEntity;
import repository.mapper.UserRoleRowMapper;

@Repository
public class UserRoleDAOImpl implements IUserRoleDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<UserRoleEntity> getUserRolesByUserId(Long userId) {
		String sql = "SELECT USER_ID, USER_ROLE FROM USER_ROLE WHERE USER_ID = ?";
		RowMapper<UserRoleEntity> rowMapper = new UserRoleRowMapper();
		List<UserRoleEntity> user = jdbcTemplate.query(sql, rowMapper, userId);
		return user;
	}

	@Override
	public void insertUserRole(Long userId, String role) {
		String sql = "INSERT INTO USER_ROLE (USER_ID, USER_ROLE) values (?, ?)";
		jdbcTemplate.update(sql, userId, role);
	}

	@Override
	public void deleteUserRole(Long userId) {
		String sql = "DELETE FROM USER_ROLE WHERE USER_ID = ?";
		jdbcTemplate.update(sql, userId);
	}

	@Override
	public void replaceRoles(Long userId, List<String> roles) {
		this.deleteUserRole(userId);
		for (String role : roles) {
			insertUserRole(userId, role);
		}
	}

}
