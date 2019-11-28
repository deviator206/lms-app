package repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import repository.entity.NotificationHistoryEntity;

public class NotificationHistoryRowMapper implements RowMapper<NotificationHistoryEntity> {

	@Override
	public NotificationHistoryEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		NotificationHistoryEntity notificationHistoryEntity = new NotificationHistoryEntity();
		notificationHistoryEntity.setId(rs.getLong("ID"));
		notificationHistoryEntity.setNotificationText(rs.getString("NOTIFICATION_TEXT"));
		notificationHistoryEntity.setOriginatorId(rs.getLong("ORIGINATOR_ID"));
		notificationHistoryEntity.setRecipientId(rs.getLong("RECIPIENT_ID"));
		notificationHistoryEntity.setOriginationDate(rs.getDate("ORIGINATION_DATE"));
		notificationHistoryEntity.setDeleted(rs.getBoolean("DELETED"));
		return notificationHistoryEntity;
	}

}
