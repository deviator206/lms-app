package repository;

import java.util.List;

import repository.entity.NotificationDetailsEntity;
import repository.entity.NotificationHistoryEntity;

public interface INotificationDAO {
	NotificationDetailsEntity getNotificationDetailsById(Long userId);

	List<NotificationDetailsEntity> getAllNotificationDetails();

	void insertNotificationDetails(NotificationDetailsEntity notification);

	void updateNotificationDetailStatus(Long userId, Boolean enabled);

	public List<NotificationDetailsEntity> getNotificationDetailsByIds(List<Long> userIds);

	public List<NotificationHistoryEntity> getAllNotifications();

	public List<NotificationHistoryEntity> getAllNotificationsbyRecipientId(Long recipientId);

	public void updateNotificationStatus(Long notificationId, Boolean read);

	public Long insertNotification(NotificationHistoryEntity notificationistory);
}
