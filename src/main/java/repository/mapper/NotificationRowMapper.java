package repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import repository.entity.NotificationDetailsEntity;

public class NotificationRowMapper implements RowMapper<NotificationDetailsEntity> {

	@Override
	public NotificationDetailsEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		NotificationDetailsEntity notificationEntity = new NotificationDetailsEntity();
		notificationEntity.setId(rs.getLong("ID"));
		notificationEntity.setUserId(rs.getLong("USERID"));
		notificationEntity.setNotificationKey(rs.getString("NOTIFICATION_KEY"));
		notificationEntity.setEnabled(rs.getBoolean("ENABLED"));
		return notificationEntity;
	}

}
