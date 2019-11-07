package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import model.FilterUserRes;
import repository.entity.UserEntity;
import repository.mapper.UserRowMapper;
import repository.mapper.UserSummaryRowMapper;
import security.SqlSafeUtil;

@Repository
@Scope("prototype")
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
				ps.setString(4, user.getEmail());
				ps.setString(5, user.getUserDisplayName());
				ps.setString(6, user.getBusinessUnit());
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().longValue();
	}

	@Override
	public UserEntity getUserByUserName(String userName) {

		if (!SqlSafeUtil.isSqlInjectionSafe(userName)) {
			throw new RuntimeException("SQL Injection Error");
		}

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

	@Override
	public void updateUser(UserEntity user) {
		String sql = "UPDATE USERS SET USERNAME = ?, ENABLED = ?, PASSWORD = ?, EMAIL = ?, USER_DISPLAY_NAME = ?, BU = ? WHERE ID = ?;";
		int affectedRows = jdbcTemplate.update(sql, user.getUserName(), user.isEnabled(), user.getPassword(),
				user.getEmail(), user.getUserDisplayName(), user.getBusinessUnit(), user.getId());
		// return affectedRows == 0 ? false : true;

	}

	@Override
	public void disableUser(UserEntity user) {
		String sql = "UPDATE USERS SET ENABLED = ? WHERE ID = ?;";
		int affectedRows = jdbcTemplate.update(sql, user.isEnabled(), user.getId());
		// return affectedRows == 0 ? false : true;

	}

	@Override
	public List<UserEntity> getUserDetailsByBuAndRole(String businessUnit, String role) {

		if (!SqlSafeUtil.isSqlInjectionSafe(businessUnit)) {
			throw new RuntimeException("SQL Injection Error");
		}

		if (!SqlSafeUtil.isSqlInjectionSafe(role)) {
			throw new RuntimeException("SQL Injection Error");
		}

		String sql = "SELECT USERS.ID, USERS.USERNAME, USERS.EMAIL, USERS.ENABLED, USERS.USER_DISPLAY_NAME, USERS.BU FROM USERS, USER_ROLE WHERE USERS.BU = ? AND USER_ROLE.USER_ROLE = ? AND USERS.ID = USER_ROLE.USER_ID";
		RowMapper<UserEntity> rowMapper = new UserSummaryRowMapper();
		return this.jdbcTemplate.query(sql, rowMapper, businessUnit, role);
	}

	@Override
	public List<UserEntity> getUserDetailsByBu(String businessUnit) {

		if (!SqlSafeUtil.isSqlInjectionSafe(businessUnit)) {
			throw new RuntimeException("SQL Injection Error");
		}

		String sql = "SELECT USERS.ID, USERS.USERNAME, USERS.EMAIL, USERS.ENABLED, USERS.USER_DISPLAY_NAME, USERS.BU FROM USERS, USER_ROLE WHERE USERS.BU = ? AND USERS.ID = USER_ROLE.USER_ID";
		RowMapper<UserEntity> rowMapper = new UserSummaryRowMapper();
		return this.jdbcTemplate.query(sql, rowMapper, businessUnit);
	}

	@Override
	public List<UserEntity> filterUsers(FilterUserRes filterUserRes) {

		if (!filterUserRes.isSqlInjectionSafe()) {
			throw new RuntimeException("SQL Injection Error");
		}

		String query = "SELECT ID, USERNAME, EMAIL, ENABLED, USER_DISPLAY_NAME, BU FROM USERS WHERE 1 = 1";
		RowMapper<UserEntity> rowMapper = new UserSummaryRowMapper();
		query = getFilterLeadsQuery(filterUserRes, query);
		return this.jdbcTemplate.query(query, rowMapper);
	}

	private String getFilterLeadsQuery(FilterUserRes filterUserRes, String query) {
		if (filterUserRes.getUserName() != null && !filterUserRes.getUserName().isEmpty()) {
			query = query + " AND USERNAME = '" + filterUserRes.getUserName() + "'";
		}

		if (filterUserRes.getUserDisplayName() != null && !filterUserRes.getUserDisplayName().isEmpty()) {
			query = query + " AND USER_DISPLAY_NAME LIKE '%" + filterUserRes.getUserDisplayName() + "%'";
		}

		if (filterUserRes.getEmail() != null && !filterUserRes.getEmail().isEmpty()) {
			query = query + " AND EMAIL = '" + filterUserRes.getEmail() + "'";
		}

		if (filterUserRes.getBusinessUnit() != null && !filterUserRes.getBusinessUnit().isEmpty()) {
			query = query + " AND BU = '" + filterUserRes.getBusinessUnit() + "'";
		}

		return query;
	}

}
