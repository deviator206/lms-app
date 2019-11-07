package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import repository.entity.NotificationDetailsEntity;
import repository.entity.NotificationHistoryEntity;
import repository.mapper.NotificationHistoryRowMapper;
import repository.mapper.NotificationRowMapper;

@Repository
@Scope("prototype")
public class NotificationDAO implements INotificationDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public NotificationDetailsEntity getNotificationDetailsById(Long userId) {
		String sql = "SELECT ID, USERID, NOTIFICATION_KEY, ENABLED FROM NOTIFICATION WHERE USERID = ?";
		RowMapper<NotificationDetailsEntity> rowMapper = new NotificationRowMapper();
		return this.jdbcTemplate.queryForObject(sql, rowMapper, new Object[] { userId });
	}

	@Override
	public List<NotificationDetailsEntity> getNotificationDetailsByIds(List<Long> userIds) {
		RowMapper<NotificationDetailsEntity> rowMapper = new NotificationRowMapper();
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
				jdbcTemplate.getDataSource());
		String sql = "SELECT ID, USERID, NOTIFICATION_KEY, ENABLED FROM NOTIFICATION WHERE USERID IN(:params)";
		List<NotificationDetailsEntity> result = namedParameterJdbcTemplate.query(sql,
				Collections.singletonMap("params", userIds), rowMapper);
		return result;
	}

	@Override
	public List<NotificationDetailsEntity> getAllNotificationDetails() {
		String sql = "SELECT ID, USERID, NOTIFICATION_KEY, ENABLED FROM NOTIFICATION";
		RowMapper<NotificationDetailsEntity> rowMapper = new NotificationRowMapper();
		return this.jdbcTemplate.query(sql, rowMapper);
	}

	@Override
	public void insertNotificationDetails(NotificationDetailsEntity notification) {
		String sql = "INSERT INTO NOTIFICATION (USERID, NOTIFICATION_KEY, ENABLED) VALUES (?,?,?)";
		jdbcTemplate.update(sql, notification.getUserId(), notification.getNotificationKey(), notification.isEnabled());
	}

	@Override
	public void updateNotificationDetailStatus(Long userId, Boolean enabled) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<NotificationHistoryEntity> getAllNotifications() {
		String sql = "SELECT ID, NOTIFICATION_TEXT,TYPE, DELETED, RECIPIENT_ID, ORIGINATOR_ID, ORIGINATION_DATE FROM NOTIFICATION_HISTORY";
		RowMapper<NotificationHistoryEntity> rowMapper = new NotificationHistoryRowMapper();
		return this.jdbcTemplate.query(sql, rowMapper);
	}

	@Override
	public List<NotificationHistoryEntity> getAllNotificationsbyRecipientId(Long recipientId) {
		String sql = "SELECT ID,NOTIFICATION_TEXT,TYPE, DELETED, RECIPIENT_ID, ORIGINATOR_ID, ORIGINATION_DATE FROM NOTIFICATION_HISTORY WHERE RECIPIENT_ID = ?";
		RowMapper<NotificationHistoryEntity> rowMapper = new NotificationHistoryRowMapper();
		return this.jdbcTemplate.query(sql, rowMapper, new Object[] { recipientId });
	}

	@Override
	public void updateNotificationStatus(Long notificationId, Boolean read) {
		String sql = "UPDATE NOTIFICATION_HISTORY SET DELETED = ? WHERE ID = ?;";
		jdbcTemplate.update(sql, read, notificationId);
	}

	@Override
	public Long insertNotification(NotificationHistoryEntity notificationistory) {
		String sql = "INSERT INTO NOTIFICATION_HISTORY (NOTIFICATION_TEXT, DELETED, RECIPIENT_ID, ORIGINATOR_ID, ORIGINATION_DATE,TYPE) VALUES (?,?,?,?,?,?) ";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, notificationistory.getNotificationText());
				ps.setBoolean(2, false);
				if (notificationistory.getRecipientId() != null) {
					ps.setLong(3, notificationistory.getRecipientId());
				} else {
					ps.setNull(3, Types.DOUBLE);
				}
				if (notificationistory.getOriginatorId() != null) {
					ps.setLong(4, notificationistory.getOriginatorId());
				} else {
					ps.setNull(4, Types.DOUBLE);
				}
				ps.setDate(5,
						notificationistory.getOriginationDate() != null
								? new java.sql.Date(notificationistory.getOriginationDate().getTime())
								: new java.sql.Date(new Date().getTime()));
				ps.setString(6, notificationistory.getNotificationType());
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().longValue();
	}

}
