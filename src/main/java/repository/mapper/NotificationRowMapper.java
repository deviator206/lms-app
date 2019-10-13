package repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import repository.entity.NotificationEntity;

public class NotificationRowMapper implements RowMapper<NotificationEntity> {

	@Override
	public NotificationEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		NotificationEntity notificationEntity = new NotificationEntity();
		notificationEntity.setId(rs.getLong("ID"));
		notificationEntity.setUserId(rs.getLong("USERID"));
		notificationEntity.setNotificationKey(rs.getString("NOTIFICATION_KEY"));
		notificationEntity.setEnabled(rs.getBoolean("ENABLED"));
		return notificationEntity;
	}

}
