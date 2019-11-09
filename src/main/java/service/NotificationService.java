package service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import consts.LeadManagementConstants;
import model.LeadRes;
import model.MarketIntelligenceInfoRes;
import model.NotificationDetails;
import model.NotificationHistory;
import model.Pagination;
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

	@Autowired
	private ILeadDetailService leadDetailService;

	@Autowired
	private IMarketIntelligenceService marketIntelligenceService;

	@Value("${app.pushNotification.lead.create.message}")
	private String leadCreationNotificationMessage;

	@Value("${app.pushNotification.lead.update.message}")
	private String leadUpdateNotificationMessage;

	@Value("${app.pushNotification.mi.create.message}")
	private String miCreateNotificationMessage;

	@Value("${app.pushNotification.mi.update.message}")
	private String miUpdateNotificationMessage;

	@Value("${app.pushNotification.mi-lead.create.message}")
	private String miToLeadCreateNotificationMessage;

	// @Autowired
	// private IMarketIntelligenceService marketIntelligenceService;

	// @Autowired
	// private ILeadDetailService leadDetailService;

	@Autowired
	private IUserService userService;

	// private ArrayList<String> keys = new ArrayList<>(Arrays.asList(
	// "c2lnJjywOqM:APA91bHVZMnktCuvYDA2a6VLwReA3eO6aYIvC0S7QUJFA3UewUI2wxEknAGhJjp38nCN1UQ9nJEN8tXHKv4klnm1JYA4KTDCjWNkLVm_B-4mXseObzPawI8zs2aCiCFJGEmnfJQC91eB"));

	@Override
	public NotificationDetails getNotificationDetailsById(Long userId) {
		NotificationDetails notificationDetails = new NotificationDetails();
		NotificationDetailsEntity notificationDetailsEntity = notificationDAO.getNotificationDetailsByUserId(userId);
		if (notificationDetailsEntity != null) {
			ModelEntityMappers.mapNotificationDetailsEntityToNotificationDetails(notificationDetailsEntity,
					notificationDetails);
		}
		return notificationDetails;
	}

	@Override
	public List<NotificationDetails> getAllNotificationDetails() {
		List<NotificationDetails> notificationDetailsLst = new ArrayList<NotificationDetails>();
		NotificationDetails notificationDetails = null;
		List<NotificationDetailsEntity> notificationDetailsEntityLst = notificationDAO.getAllNotificationDetails();
		if (notificationDetailsEntityLst != null && !notificationDetailsEntityLst.isEmpty()) {
			for (NotificationDetailsEntity notificationDetailsEntity : notificationDetailsEntityLst) {
				notificationDetails = new NotificationDetails();
				ModelEntityMappers.mapNotificationDetailsEntityToNotificationDetails(notificationDetailsEntity,
						notificationDetails);
				notificationDetailsLst.add(notificationDetails);
			}
		}
		return notificationDetailsLst;
	}

	@Override
	public void addNotificationDetails(NotificationDetails notification) {
		NotificationDetailsEntity notificationDetailsEntity = new NotificationDetailsEntity();
		ModelEntityMappers.mapNotificationDetailsToNotificationDetailsEntity(notification, notificationDetailsEntity);
		notificationDAO.insertNotificationDetails(notificationDetailsEntity);
	}

	@Override
	public void updateNotificationDetails(NotificationDetails notification) {
		NotificationDetailsEntity notificationDetailsEntity = new NotificationDetailsEntity();
		ModelEntityMappers.mapNotificationDetailsToNotificationDetailsEntity(notification, notificationDetailsEntity);
		if (notification.getUserId() != null) {
			notificationDAO.updateNotificationDetailsByUserId(notificationDetailsEntity);
		}
	}

	@Override
	public void sendNotificationAfterLeadCreation(Long leadCreatorId, List<Long> createdLeadIds) {
		List<Long> userIds = new ArrayList<Long>();

		// userIds.add(leadCreatorId);

		List<Long> adminAndUsrBuHeadIdList = this.getAdminAndUserBuHeadIds(leadCreatorId);
		if (adminAndUsrBuHeadIdList != null && !adminAndUsrBuHeadIdList.isEmpty()) {
			userIds.addAll(adminAndUsrBuHeadIdList);
		}

		List<Long> leadBuHeadLst = getLeadsBuHeadAndSalesRepUserIds(createdLeadIds);
		if (leadBuHeadLst != null && !leadBuHeadLst.isEmpty()) {
			userIds.addAll(leadBuHeadLst);
		}

		Set<Long> uniqueUserSet = new HashSet<Long>(userIds);
		userIds = new ArrayList<Long>(uniqueUserSet);

		List<String> stringsList = createdLeadIds.stream().map(Object::toString).collect(Collectors.toList());

		addAndSendNotificationForRecipients(leadCreatorId, userIds,
				LeadManagementConstants.NOTIFICATION_TYPE_LEAD_CREATED,
				String.format(leadCreationNotificationMessage, String.join(", ", stringsList)));
	}

	private List<Long> getAdminAndUserBuHeadIds(Long userId) {
		List<Long> userIds = new ArrayList<Long>();
		List<Long> adminUsrList = this.getAdminUserIds();
		if (adminUsrList != null && !adminUsrList.isEmpty()) {
			userIds.addAll(adminUsrList);
		}

		List<Long> buHeadLst = this.getUserBuHeadUserIds(userId);
		if (buHeadLst != null && !buHeadLst.isEmpty()) {
			userIds.addAll(buHeadLst);
		}

		Set<Long> uniqueUserSet = new HashSet<Long>(userIds);
		userIds = new ArrayList<Long>(uniqueUserSet);

		return userIds;
	}

	private void addAndSendNotificationForRecipients(Long originatorId, List<Long> recipientIds,
			String notificationType, String notificationMessage) {
		if (recipientIds != null && !recipientIds.isEmpty()) {
			addNotificationForRecipients(originatorId, recipientIds, notificationType, notificationMessage);
			List<String> notifikationKeys = getUserNotificationKeys(recipientIds);
			sendPushNotifications(notifikationKeys, notificationMessage);
		}
	}

	private List<String> getUserNotificationKeys(List<Long> userIds) {
		List<NotificationDetailsEntity> notificationDetailsList = notificationDAO.getNotificationDetailsByIds(userIds);

		List<String> notifikationKeys = new ArrayList<String>();
		for (NotificationDetailsEntity notificationEntity : notificationDetailsList) {
			notifikationKeys.add(notificationEntity.getNotificationKey());
		}
		return notifikationKeys;
	}

	@Override
	public void sendNotificationAfterLeadUpdate(Long updatorId, Long leadId) {
		List<Long> userIds = new ArrayList<Long>();

		// userIds.add(leadCreatorId);

		List<Long> adminAndUsrBuHeadIdList = this.getAdminAndUserBuHeadIds(updatorId);
		if (adminAndUsrBuHeadIdList != null && !adminAndUsrBuHeadIdList.isEmpty()) {
			userIds.addAll(adminAndUsrBuHeadIdList);
		}

		// Lead BU Head
		List<Long> leadBuHeadLst = this.getLeadBuHeadAndSalesRepUserIds(leadId);
		if (leadBuHeadLst != null && !leadBuHeadLst.isEmpty()) {
			userIds.addAll(leadBuHeadLst);
		}

		Set<Long> uniqueUserSet = new HashSet<Long>(userIds);
		userIds = new ArrayList<Long>(uniqueUserSet);

		addAndSendNotificationForRecipients(updatorId, userIds, LeadManagementConstants.NOTIFICATION_TYPE_LEAD_UPDATED,
				String.format(leadUpdateNotificationMessage, leadId));
	}

	@Override
	public void sendNotificationAfterMiCreation(Long miId, Long miCreatorId) {
		List<Long> userIds = new ArrayList<Long>();

		List<Long> adminAndUsrBuHeadIdList = this.getAdminAndUserBuHeadIds(miCreatorId);
		if (adminAndUsrBuHeadIdList != null && !adminAndUsrBuHeadIdList.isEmpty()) {
			userIds.addAll(adminAndUsrBuHeadIdList);
		}

		List<Long> minInfoCreatorsLst = getMiInfoCreatorIds(miId);
		if (minInfoCreatorsLst != null && !minInfoCreatorsLst.isEmpty()) {
			userIds.addAll(minInfoCreatorsLst);
		}

		Set<Long> uniqueUserSet = new HashSet<Long>(userIds);
		userIds = new ArrayList<Long>(uniqueUserSet);

		addAndSendNotificationForRecipients(miCreatorId, userIds, LeadManagementConstants.NOTIFICATION_TYPE_MI_CREATED,
				String.format(miCreateNotificationMessage, miId));

	}

	@Override
	public void sendNotificationAfterMiUpdate(Long miId, Long miUpdatorId) {
		List<Long> userIds = new ArrayList<Long>();

		List<Long> adminAndUsrBuHeadIdList = this.getAdminAndUserBuHeadIds(miUpdatorId);
		if (adminAndUsrBuHeadIdList != null && !adminAndUsrBuHeadIdList.isEmpty()) {
			userIds.addAll(adminAndUsrBuHeadIdList);
		}

		List<Long> minInfoCreatorsLst = getMiInfoCreatorIds(miId);
		if (minInfoCreatorsLst != null && !minInfoCreatorsLst.isEmpty()) {
			userIds.addAll(minInfoCreatorsLst);
		}

		Set<Long> uniqueUserSet = new HashSet<Long>(userIds);
		userIds = new ArrayList<Long>(uniqueUserSet);

		addAndSendNotificationForRecipients(miUpdatorId, userIds, LeadManagementConstants.NOTIFICATION_TYPE_MI_UPDATED,
				String.format(miUpdateNotificationMessage, miId));

	}

	@Override
	public void sendNotificationAfterMiToLeadCreation(Long miId, Long leadCreatorId, Long rootLeadId) {
		List<Long> userIds = new ArrayList<Long>();

		List<Long> adminAndUsrBuHeadIdList = this.getAdminAndUserBuHeadIds(leadCreatorId);
		if (adminAndUsrBuHeadIdList != null && !adminAndUsrBuHeadIdList.isEmpty()) {
			userIds.addAll(adminAndUsrBuHeadIdList);
		}

		// Lead BU Head
		List<Long> leadBuHeadLst = this.getLeadsBuHeadAndSalesRepUserIdsByRootLeadId(rootLeadId);
		if (leadBuHeadLst != null && !leadBuHeadLst.isEmpty()) {
			userIds.addAll(leadBuHeadLst);
		}

		List<Long> minInfoCreatorsLst = getMiInfoCreatorIds(miId);
		if (minInfoCreatorsLst != null && !minInfoCreatorsLst.isEmpty()) {
			userIds.addAll(minInfoCreatorsLst);
		}

		Set<Long> uniqueUserSet = new HashSet<Long>(userIds);
		userIds = new ArrayList<Long>(uniqueUserSet);

		List<Long> leadIds = new ArrayList<Long>();
		List<LeadRes> leads = leadDetailService.getLeadsByRoot(rootLeadId);
		if (leads != null) {
			for (LeadRes leadRes : leads) {
				leadIds.add(leadRes.getId());
			}
		}
		List<String> stringsList = leadIds.stream().map(Object::toString).collect(Collectors.toList());

		addAndSendNotificationForRecipients(leadCreatorId, userIds,
				LeadManagementConstants.NOTIFICATION_TYPE_MI_TO_LEAD_CREATED,
				String.format(miToLeadCreateNotificationMessage, stringsList, miId));

	}

	private List<Long> getMiInfoCreatorIds(Long miId) {
		List<Long> userIds = new ArrayList<Long>();
		List<MarketIntelligenceInfoRes> miInfiLst = marketIntelligenceService.getMarketIntelligenceInfo(miId, null);
		if (miInfiLst != null) {
			for (MarketIntelligenceInfoRes miInfoRes : miInfiLst) {
				Long miInfoCreatorId = miInfoRes.getCreatorId();
				if (miInfoCreatorId != null) {
					userIds.add(miInfoCreatorId);
				}
			}
		}

		Set<Long> uniqueUserSet = new HashSet<Long>(userIds);
		userIds = new ArrayList<Long>(uniqueUserSet);
		return userIds;
	}

	private List<Long> getAdminUserIds() {
		List<Long> userIds = new ArrayList<Long>();

		List<String> roleList = new ArrayList<String>();
		roleList.add(LeadManagementConstants.ROLE_ADMIN);

		List<UserRes> adminLst = userService.getUsersByRoles(roleList);

		for (UserRes user : adminLst) {
			userIds.add(user.getUserId());
		}

		Set<Long> uniqueUserSet = new HashSet<Long>(userIds);
		userIds = new ArrayList<Long>(uniqueUserSet);

		return userIds;
	}

	private List<Long> getBuHeadUserIds(String businessUnit) {
		List<Long> userIds = new ArrayList<Long>();
		List<UserRes> buHeadLst = userService.getUserDetailsByBuAndRole(businessUnit,
				LeadManagementConstants.ROLE_BU_HEAD);
		if (buHeadLst != null) {
			for (UserRes user : buHeadLst) {
				userIds.add(user.getUserId());
			}
		}
		Set<Long> uniqueUserSet = new HashSet<Long>(userIds);
		userIds = new ArrayList<Long>(uniqueUserSet);
		return userIds;
	}

	private List<Long> getUserBuHeadUserIds(Long userId) {
		User leadCreatorUser = userService.getUserByUserId(userId);
		String leadCreatorBU = leadCreatorUser.getBusinessUnit();
		return this.getBuHeadUserIds(leadCreatorBU);
	}

	private List<Long> getLeadBuHeadAndSalesRepUserIds(Long leadId) {
		List<Long> userIds = new ArrayList<Long>();
		List<Long> leadBuHeadLst = new ArrayList<Long>();
		List<Long> leadBuHeadLstTmp = null;
		LeadRes leadRes = leadDetailService.getLead(leadId);

		// Assigned Sales Rep of Lead
		if (leadRes.getLeadsSummaryRes() != null) {
			if (leadRes.getLeadsSummaryRes().getSalesRepId() != null) {
				userIds.add(leadRes.getLeadsSummaryRes().getSalesRepId());
			} else if (leadRes.getLeadsSummaryRes().getSalesRep() != null
					&& leadRes.getLeadsSummaryRes().getSalesRep().getUserId() != null) {
				userIds.add(leadRes.getLeadsSummaryRes().getSalesRep().getUserId());
			}
		}

		List<String> leadBULst = leadRes.getLeadsSummaryRes().getBusinessUnits();
		if (leadBULst != null) {
			for (String buTmp : leadBULst) {
				leadBuHeadLstTmp = this.getBuHeadUserIds(buTmp);
				if (leadBuHeadLstTmp != null && !leadBuHeadLstTmp.isEmpty()) {
					leadBuHeadLst.addAll(leadBuHeadLstTmp);
				}
			}
		}

		Set<Long> uniqueUserSet = new HashSet<Long>(userIds);
		userIds = new ArrayList<Long>(uniqueUserSet);

		return userIds;
	}

	private List<Long> getLeadsBuHeadAndSalesRepUserIds(List<Long> leadIds) {
		List<Long> userIds = new ArrayList<Long>();
		List<Long> leadBuHeadLstTmp = null;
		if (leadIds != null && !leadIds.isEmpty()) {
			for (Long leadId : leadIds) {
				leadBuHeadLstTmp = this.getLeadBuHeadAndSalesRepUserIds(leadId);
				if (leadBuHeadLstTmp != null && !leadBuHeadLstTmp.isEmpty()) {
					userIds.addAll(leadBuHeadLstTmp);
				}
			}
		}

		Set<Long> uniqueUserSet = new HashSet<Long>(userIds);
		userIds = new ArrayList<Long>(uniqueUserSet);
		return userIds;
	}

	private List<Long> getLeadsBuHeadAndSalesRepUserIdsByRootLeadId(Long rootLeadId) {
		List<Long> userIds = new ArrayList<Long>();
		List<Long> leadIds = new ArrayList<Long>();
		List<LeadRes> leads = leadDetailService.getLeadsByRoot(rootLeadId);
		if (leads != null) {
			for (LeadRes leadRes : leads) {
				leadIds.add(leadRes.getId());
			}
			if (!leadIds.isEmpty()) {
				userIds = getLeadsBuHeadAndSalesRepUserIds(leadIds);
			}
		}
		return userIds;
	}

	public void addNotificationForRecipients(Long originatorId, List<Long> recipientIds, String type, String message) {
		NotificationHistory notificationHistory = null;
		List<NotificationHistory> NotificationLst = new ArrayList<NotificationHistory>();
		for (Long usrId : recipientIds) {
			notificationHistory = new NotificationHistory();
			notificationHistory.setOriginatorId(originatorId);
			notificationHistory.setRecipientId(usrId);
			notificationHistory.setDeleted(false);
			notificationHistory.setNotificationType(type);
			notificationHistory.setNotificationText(message);
			NotificationLst.add(notificationHistory);
		}
		addNotifications(NotificationLst);
	}

	@Override
	public List<NotificationHistory> getNotifications(Long recipientId, Pagination pagination) {
		List<NotificationHistoryEntity> notificationHistoryEntityLst = null;
		List<NotificationHistory> notificationHistoryLst = new ArrayList<NotificationHistory>();
		if (recipientId == null) {
			notificationHistoryEntityLst = notificationDAO.getAllNotifications(pagination);
		} else {
			notificationHistoryEntityLst = notificationDAO.getAllNotificationsbyRecipientId(recipientId, pagination);
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
	public void sendPushNotifications(List<String> notifikationKeys, String message) {
		pushNotificationService.sendNotificationMulti(notifikationKeys, message);
	}

}
