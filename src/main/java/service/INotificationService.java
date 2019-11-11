package service;

import java.util.List;

import model.NotificationDetails;
import model.NotificationHistory;
import model.Pagination;

public interface INotificationService {
	NotificationDetails getNotificationDetailsById(Long userId);

	public List<NotificationDetails> getAllNotificationDetails();

	void addNotificationDetails(NotificationDetails notification);

	void updateNotificationDetails(NotificationDetails notification);

	void sendNotificationAfterLeadCreation(Long leadCreatorId, List<Long> createdLeadIds);

	public void sendNotificationAfterMiToLeadCreation(Long miId, Long leadCreatorId, Long rootLeadId);

	public void sendNotificationAfterMiCreation(Long miId, Long miCreatorId);

	public List<NotificationHistory> getNotifications(Long recipientId, Pagination pagination);

	public void updateNotificationStatus(Long notificationId, Boolean read);

	public Long addNotification(NotificationHistory notificationistory);

	public List<Long> addNotifications(List<NotificationHistory> notificationHistories);

	public void updateNotifications(List<NotificationHistory> notificationHistories);

	public void sendNotificationAfterLeadUpdate(Long updatorId, Long leadId);

	public void sendCustomNotificationAfterLeadUpdate(Long updatorId, Long leadId, String customMessage);

	public void sendNotificationAfterMiUpdate(Long miId, Long miUpdatorId);

}
