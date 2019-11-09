package service;

import java.util.List;

import model.NotificationHistory;
import model.Pagination;
import repository.entity.NotificationDetailsEntity;

public interface INotificationService {
	NotificationDetailsEntity getNotificationDetailsById(Long userId);

	List<NotificationDetailsEntity> getAllNotificationDetails();

	void addNotificationDetails(NotificationDetailsEntity notification);

	void updateNotificationDetailStatus(Long userId, Boolean enabled);

	void sendNotificationAfterLeadCreation(Long leadCreatorId, List<Long> createdLeadIds);

	public void sendNotificationAfterMiToLeadCreation(Long leadCreatorId, Long rootLeadId);
	public void sendNotificationAfterMiCreation(Long miCreatorId, Long miId);

	public List<NotificationHistory> getNotifications(Long recipientId,Pagination pagination);

	public void updateNotificationStatus(Long notificationId, Boolean read);

	public Long addNotification(NotificationHistory notificationistory);

	public List<Long> addNotifications(List<NotificationHistory> notificationHistories);

	public void updateNotifications(List<NotificationHistory> notificationHistories);

	public void sendNotificationAfterLeadUpdate(Long updatorId, Long leadId);
}
