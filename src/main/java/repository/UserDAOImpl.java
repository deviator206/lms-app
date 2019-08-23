package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import repository.entity.UserEntity;
import repository.mapper.UserRowMapper;

@Repository
public class UserDAOImpl implements IUserDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	@Override
	public List<UserEntity> getUsers() {
		String sql = "SELECT ID, USERNAME, PASSWORD, EMAIL, ENABLED, USER_DISPLAY_NAME, BU FROM USERS";
		RowMapper<UserEntity> rowMapper = new UserRowMapper();
		return this.jdbcTemplate.query(sql, rowMapper);
	}

	@Override
	public Long insertUser(UserEntity user) {
		String sql = "INSERT INTO USERS (USERNAME, ENABLED, PASSWORD, EMAIL, USER_DISPLAY_NAME, BU) values (?, ?, ?, ?, ?, ?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, user.getUserName());
				ps.setBoolean(2, user.isEnabled());
				ps.setString(3, user.getPassword());
				ps.setString(4,user.getEmail());
				ps.setString(5, user.getUserDisplayName());
				ps.setString(6, user.getBusinessUnit());
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().longValue();	
	}

	@Override
	public UserEntity getUserByUserName(String userName) {
		String sql = "SELECT ID, USERNAME,PASSWORD, EMAIL, ENABLED, USER_DISPLAY_NAME, BU FROM USERS WHERE USERNAME = ?";
		RowMapper<UserEntity> rowMapper = new UserRowMapper();
		UserEntity user = jdbcTemplate.queryForObject(sql, rowMapper, userName);
		return user;
	}

	@Override
	public UserEntity getUserByUserId(Long userId) {
		String sql = "SELECT ID, USERNAME,PASSWORD, EMAIL, ENABLED, USER_DISPLAY_NAME, BU FROM USERS WHERE ID = ?";
		RowMapper<UserEntity> rowMapper = new UserRowMapper();
		UserEntity user = jdbcTemplate.queryForObject(sql, rowMapper, userId);
		return user;
	}

}
