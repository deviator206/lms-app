package repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import repository.entity.UserEntity;
import repository.mapper.UserRowMapper;

@Repository
public class UserDAOImpl implements IUserDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	@Override
	public List<UserEntity> getUsers() {
		String sql = "SELECT ID, USERNAME, PASSWORD, EMAIL, ENABLED FROM USERS";
		RowMapper<UserEntity> rowMapper = new UserRowMapper();
		return this.jdbcTemplate.query(sql, rowMapper);
	}

	@Override
	public void createUser(UserEntity user) {
		String sql = "INSERT INTO USERS (USERNAME, ENABLED, PASSWORD, EMAIL) values (?, ?, ?, ?)";
		jdbcTemplate.update(sql, user.getUserName(), user.isEnabled(), user.getPassword(), user.getEmail());
	}

	@Override
	public UserEntity getUserByUserName(String userName) {
		String sql = "SELECT ID, USERNAME,PASSWORD, EMAIL, ENABLED FROM USERS WHERE USERNAME = ?";
		RowMapper<UserEntity> rowMapper = new UserRowMapper();
		UserEntity user = jdbcTemplate.queryForObject(sql, rowMapper, userName);
		return user;
	}

	@Override
	public UserEntity getUserByUserId(Long userId) {
		String sql = "SELECT ID, USERNAME,PASSWORD, EMAIL, ENABLED FROM USERS WHERE ID = ?";
		RowMapper<UserEntity> rowMapper = new UserRowMapper();
		UserEntity user = jdbcTemplate.queryForObject(sql, rowMapper, userId);
		return user;
	}

}
