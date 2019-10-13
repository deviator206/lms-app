package service;

import java.util.List;

import repository.entity.NotificationEntity;

public interface INotificationService {
	NotificationEntity getNotificationDetailsById(Long userId);

	List<NotificationEntity> getAllNotificationDetails();

	void addNotificationDetails(NotificationEntity notification);

	void updateNotificationStatus(Long userId, Boolean enabled);
	
	void sendNotificationAfterLeadCreation(Long leadCreatorId, List<Long> createdLeadIds);
	public void sendNotificationAfterMiToLeadCreation(Long leadCreatorId, Long rootLeadId);
}
