package repository;

import java.util.List;

import repository.entity.NotificationEntity;

public interface INotificationDAO {
	NotificationEntity getNotificationDetailsById(Long userId);

	List<NotificationEntity> getAllNotificationDetails();

	void insertNotificationDetails(NotificationEntity notification);

	void updateNotificationStatus(Long userId, Boolean enabled);
	
	public List<NotificationEntity> getNotificationDetailsByIds(List<Long> userIds);
}
