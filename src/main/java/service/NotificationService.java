package service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import consts.LeadManagementConstants;
import model.LeadRes;
import model.User;
import model.UserRes;
import repository.INotificationDAO;
import repository.entity.NotificationEntity;

@Service
public class NotificationService implements INotificationService {

	@Autowired
	private INotificationDAO notificationDAO;
	
	@Autowired
	private ILeadDetailService leadDetailService;

	@Autowired
	private IUserService userService;

	@Override
	public NotificationEntity getNotificationDetailsById(Long userId) {
		return notificationDAO.getNotificationDetailsById(userId);
	}

	@Override
	public List<NotificationEntity> getAllNotificationDetails() {
		return notificationDAO.getAllNotificationDetails();
	}

	@Override
	public void addNotificationDetails(NotificationEntity notification) {
		notificationDAO.insertNotificationDetails(notification);
	}

	@Override
	public void updateNotificationStatus(Long userId, Boolean enabled) {
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

		List<NotificationEntity> notificationDetailsList = notificationDAO.getNotificationDetailsByIds(userIds);

		List<String> notifikationKeys = new ArrayList<String>();
		for (NotificationEntity notificationEntity : notificationDetailsList) {
			notifikationKeys.add(notificationEntity.getNotificationKey());
		}
		
		
		
	}
	
	@Override
	public void sendNotificationAfterMiToLeadCreation(Long leadCreatorId,  Long rootLeadId) {
		List<LeadRes> createdLeadIds = leadDetailService.getLeadsByRoot(rootLeadId);
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

		List<NotificationEntity> notificationDetailsList = notificationDAO.getNotificationDetailsByIds(userIds);

		List<String> notifikationKeys = new ArrayList<String>();
		for (NotificationEntity notificationEntity : notificationDetailsList) {
			notifikationKeys.add(notificationEntity.getNotificationKey());
		}
		
		
		
	}

}
