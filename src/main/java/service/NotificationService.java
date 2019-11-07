package service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import consts.LeadManagementConstants;
import model.NotificationHistory;
import model.User;
import model.UserRes;
import repository.INotificationDAO;
import repository.entity.NotificationDetailsEntity;
import repository.entity.NotificationHistoryEntity;
import repository.mapper.ModelEntityMappers;

@Service
@Scope("prototype")
public class NotificationService implements INotificationService {

	@Autowired
	private INotificationDAO notificationDAO;

	@Autowired
	private IPushNotificationService pushNotificationService;

	// @Autowired
	// private ILeadDetailService leadDetailService;

	@Autowired
	private IUserService userService;

	private ArrayList<String> keys = new ArrayList<>(Arrays.asList(
			"c2lnJjywOqM:APA91bHVZMnktCuvYDA2a6VLwReA3eO6aYIvC0S7QUJFA3UewUI2wxEknAGhJjp38nCN1UQ9nJEN8tXHKv4klnm1JYA4KTDCjWNkLVm_B-4mXseObzPawI8zs2aCiCFJGEmnfJQC91eB"));

	@Override
	public NotificationDetailsEntity getNotificationDetailsById(Long userId) {
		return notificationDAO.getNotificationDetailsById(userId);
	}

	@Override
	public List<NotificationDetailsEntity> getAllNotificationDetails() {
		return notificationDAO.getAllNotificationDetails();
	}

	@Override
	public void addNotificationDetails(NotificationDetailsEntity notification) {
		notificationDAO.insertNotificationDetails(notification);
	}

	@Override
	public void updateNotificationDetailStatus(Long userId, Boolean enabled) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendNotificationAfterLeadCreation(Long leadCreatorId, List<Long> createdLeadIds) {
		User leadCreatorUser = userService.getUserByUserId(leadCreatorId);
		String leadCreatorBU = leadCreatorUser.getBusinessUnit();
		List<String> roleList = new ArrayList<String>();
		roleList.add(LeadManagementConstants.ROLE_ADMIN);
		List<UserRes> adminLst = userService.getUsersByRoles(roleList);
		List<UserRes> buHeadLst = userService.getUserDetailsByBuAndRole(leadCreatorBU,
				LeadManagementConstants.ROLE_BU_HEAD);

		List<Long> userIds = new ArrayList<Long>();
		userIds.add(leadCreatorId);
		for (UserRes user : adminLst) {
			userIds.add(user.getUserId());
		}
		for (UserRes user : buHeadLst) {
			userIds.add(user.getUserId());
		}

		if (userIds != null && !userIds.isEmpty()) {
			addNotificationAfterLeadCreation(leadCreatorId, userIds,
					LeadManagementConstants.NOTIFICATION_TYPE_LEAD_CREATION);

			List<NotificationDetailsEntity> notificationDetailsList = notificationDAO
					.getNotificationDetailsByIds(userIds);

			List<String> notifikationKeys = new ArrayList<String>();
			for (NotificationDetailsEntity notificationEntity : notificationDetailsList) {
				notifikationKeys.add(notificationEntity.getNotificationKey());
			}

			sendPushNotifications(keys);
		}
	}

	public void addNotificationAfterLeadCreation(Long originatorId, List<Long> recipientIds, String type) {
		NotificationHistory notificationHistory = null;
		List<NotificationHistory> NotificationLst = new ArrayList<NotificationHistory>();
		for (Long usrId : recipientIds) {
			notificationHistory = new NotificationHistory();
			notificationHistory.setOriginatorId(originatorId);
			notificationHistory.setRecipientId(usrId);
			notificationHistory.setDeleted(false);
			notificationHistory.setNotificationType(type);
			NotificationLst.add(notificationHistory);
		}
		addNotifications(NotificationLst);
	}

	@Override
	public void sendNotificationAfterMiToLeadCreation(Long leadCreatorId, Long rootLeadId) {
		// List<LeadRes> createdLeadIds = leadDetailService.getLeadsByRoot(rootLeadId);
		User leadCreatorUser = userService.getUserByUserId(leadCreatorId);
		String leadCreatorBU = leadCreatorUser.getBusinessUnit();
		List<String> roleList = new ArrayList<String>();
		roleList.add(LeadManagementConstants.ROLE_ADMIN);
		List<UserRes> adminLst = userService.getUsersByRoles(roleList);
		List<UserRes> buHeadLst = userService.getUserDetailsByBuAndRole(leadCreatorBU,
				LeadManagementConstants.ROLE_BU_HEAD);

		List<Long> userIds = new ArrayList<Long>();
		userIds.add(leadCreatorId);
		for (UserRes user : adminLst) {
			userIds.add(user.getUserId());
		}
		for (UserRes user : buHeadLst) {
			userIds.add(user.getUserId());
		}

		if (userIds != null && !userIds.isEmpty()) {
			addNotificationAfterLeadCreation(leadCreatorId, userIds,
					LeadManagementConstants.NOTIFICATION_TYPE_MI_TO_LEAD_CREATION);
			List<NotificationDetailsEntity> notificationDetailsList = notificationDAO
					.getNotificationDetailsByIds(userIds);
			List<String> notifikationKeys = new ArrayList<String>();
			for (NotificationDetailsEntity notificationEntity : notificationDetailsList) {
				notifikationKeys.add(notificationEntity.getNotificationKey());
			}
			sendPushNotifications(keys);
		}
	}

	@Override
	public List<NotificationHistory> getNotifications(Long recipientId) {
		List<NotificationHistoryEntity> notificationHistoryEntityLst = null;
		List<NotificationHistory> notificationHistoryLst = new ArrayList<NotificationHistory>();
		if (recipientId == null) {
			notificationHistoryEntityLst = notificationDAO.getAllNotifications();
		} else {
			notificationHistoryEntityLst = notificationDAO.getAllNotificationsbyRecipientId(recipientId);
		}
		if (notificationHistoryEntityLst != null) {
			NotificationHistory notificationHistory = null;
			for (NotificationHistoryEntity notificationHistoryEntity : notificationHistoryEntityLst) {
				notificationHistory = new NotificationHistory();
				ModelEntityMappers.mapNotificationHistoryEntityToNotificationHistory(notificationHistoryEntity,
						notificationHistory);
				notificationHistoryLst.add(notificationHistory);
			}
		}
		return notificationHistoryLst;
	}

	@Override
	public void updateNotificationStatus(Long notificationId, Boolean read) {
		notificationDAO.updateNotificationStatus(notificationId, read);
	}

	@Override
	public void updateNotifications(List<NotificationHistory> notificationHistories) {
		for (NotificationHistory notificationHistory : notificationHistories) {
			notificationDAO.updateNotificationStatus(notificationHistory.getId(), notificationHistory.getDeleted());
		}
	}

	@Override
	public Long addNotification(NotificationHistory notificationHistory) {
		NotificationHistoryEntity notificationHistoryEntity = new NotificationHistoryEntity();
		ModelEntityMappers.mapNotificationHistoryToNotificationHistoryEntity(notificationHistory,
				notificationHistoryEntity);
		return notificationDAO.insertNotification(notificationHistoryEntity);
	}

	@Override
	public List<Long> addNotifications(List<NotificationHistory> notificationHistories) {
		List<Long> addedNotifications = new ArrayList<Long>();
		NotificationHistoryEntity notificationHistoryEntity;
		for (NotificationHistory notificationHistory : notificationHistories) {
			notificationHistoryEntity = new NotificationHistoryEntity();
			ModelEntityMappers.mapNotificationHistoryToNotificationHistoryEntity(notificationHistory,
					notificationHistoryEntity);
			addedNotifications.add(notificationDAO.insertNotification(notificationHistoryEntity));
		}
		return addedNotifications;
	}

	// @Override
	public void sendPushNotifications(List<String> notifikationKeys) {
		pushNotificationService.sendNotificationMulti(notifikationKeys, "Lead Created");
	}

}
