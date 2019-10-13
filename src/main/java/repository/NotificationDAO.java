package repository;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import repository.entity.NotificationEntity;
import repository.mapper.NotificationRowMapper;

@Repository
public class NotificationDAO implements INotificationDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public NotificationEntity getNotificationDetailsById(Long userId) {
		String sql = "SELECT ID, USERID, NOTIFICATION_KEY, ENABLED FROM NOTIFICATION WHERE USERID = ?";
		RowMapper<NotificationEntity> rowMapper = new NotificationRowMapper();
		return this.jdbcTemplate.queryForObject(sql, rowMapper, new Object[] { userId });
	}
	
	@Override
	public List<NotificationEntity> getNotificationDetailsByIds(List<Long> userIds) {		
		RowMapper<NotificationEntity> rowMapper = new NotificationRowMapper();
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
	    String sql = "SELECT ID, USERID, NOTIFICATION_KEY, ENABLED FROM NOTIFICATION WHERE USERID IN(:params)";
	    List<NotificationEntity> result = namedParameterJdbcTemplate.query(sql, Collections.singletonMap("params", userIds), rowMapper);
	    return result;		
	}

	@Override
	public List<NotificationEntity> getAllNotificationDetails() {
		String sql = "SELECT ID, USERID, NOTIFICATION_KEY, ENABLED FROM NOTIFICATION";
		RowMapper<NotificationEntity> rowMapper = new NotificationRowMapper();
		return this.jdbcTemplate.query(sql, rowMapper);
	}

	@Override
	public void insertNotificationDetails(NotificationEntity notification) {
		String sql = "INSERT INTO NOTIFICATION (USERID, NOTIFICATION_KEY, ENABLED) VALUES (?,?,?)";
		jdbcTemplate.update(sql, notification.getUserId(), notification.getNotificationKey(), notification.isEnabled());
	}

	@Override
	public void updateNotificationStatus(Long userId, Boolean enabled) {
		// TODO Auto-generated method stub

	}

}
