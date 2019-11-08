package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import model.FilterUserRes;
import model.ForgotPasswordResponse;
import model.User;
import model.UserRegistrationDetails;
import model.UserRes;
import model.UserRoles;
import repository.IUserDAO;
import repository.IUserRoleDAO;
import repository.entity.UserEntity;
import repository.entity.UserRoleEntity;
import repository.mapper.ModelEntityMappers;
import util.PasswordGenerationUtil;

@Service
@Scope("prototype")
public class UserServiceImpl implements IUserService {
	@Autowired
	private IUserDAO userDAO;

	@Value("${app.mail.userName}")
	private String mailUserName;

	@Autowired
	private IUserRoleDAO userRoleDAO;

	@Autowired
	private IMailService mailService;

	@Resource
	private PasswordEncoder passwordEncoder;

	private Map<Long, UserRes> userIdUserMap;

	@Autowired
	private PlatformTransactionManager transactionManager;

	@Override
	public List<UserRes> getUsers(String bu) {
		List<UserEntity> userEntities = null;
		if (bu != null) {
			userEntities = userDAO.getUserDetailsByBu(bu);
		} else {
			userEntities = userDAO.getUsers();
		}
		List<UserRes> users = new ArrayList<UserRes>();
		for (UserEntity userEntity : userEntities) {
			UserRes user = new UserRes();
			this.mapUserEntityToUserRes(userEntity, user);
			List<String> roles = this.getUserRoleByUserId(userEntity.getId());
			user.setRoles(roles);
			users.add(user);
		}
		return users;
	}

	@Override
	public List<UserRes> getUsersByRoles(List<String> userRoles) {
		List<UserEntity> userEntities = userDAO.getUsers();
		List<UserRes> users = new ArrayList<UserRes>();
		for (UserEntity userEntity : userEntities) {
			UserRes user = new UserRes();
			this.mapUserEntityToUserRes(userEntity, user);
			List<String> roles = this.getUserRoleByUserId(userEntity.getId());
			for (String role : userRoles) {
				if (roles.contains(role)) {
					user.setRoles(roles);
					users.add(user);
					break;
				}
			}
		}
		return users;
	}

	@Override
	public Map<Long, UserRes> getCachedUsersSummaryMap() {
		if (userIdUserMap == null) {
			userIdUserMap = new HashMap<Long, UserRes>();
			List<UserEntity> userEntities = userDAO.getUsers();
			// List<UserRes> users = new ArrayList<UserRes>();
			for (UserEntity userEntity : userEntities) {
				UserRes user = new UserRes();
				this.mapUserEntityToUserResSummary(userEntity, user);
				userIdUserMap.put(user.getUserId(), user);
			}
		}
		return userIdUserMap;
	}

	@Override
	public List<UserRes> getUserDetailsByBuAndRole(String businessUnit, String role) {
		List<UserEntity> userEntityLst = userDAO.getUserDetailsByBuAndRole(businessUnit, role);
		List<UserRes> users = new ArrayList<UserRes>();
		for (UserEntity userEntity : userEntityLst) {
			UserRes user = new UserRes();
			this.mapUserEntityToUserRes(userEntity, user);
			List<String> roles = this.getUserRoleByUserId(userEntity.getId());
			user.setRoles(roles);
			users.add(user);
		}
		return users;
	}

	/*
	 * private List<UserRes> mapUserToUserRes(List<User> users) { List<UserRes>
	 * userResList = new ArrayList<UserRes>(); for (User user : users) { UserRes
	 * userRes = new UserRes(); ModelMappers.mapUserToUserRes(user, userRes);
	 * userResList.add(userRes); }
	 * 
	 * return userResList; }
	 */

	@Override
	public Long addUser(UserRegistrationDetails userRegistrationDetails) {
		UserEntity user = new UserEntity();
		user.setUserName(userRegistrationDetails.getUserName());

		// Enabled false while adding user
		user.setEnabled(false);

		user.setUserDisplayName(userRegistrationDetails.getUserDisplayName());
		user.setBusinessUnit(userRegistrationDetails.getBusinessUnit());
		user.setEmail(userRegistrationDetails.getEmail());
		user.setPassword(passwordEncoder.encode(userRegistrationDetails.getPassword()));
		Long userId;

		TransactionStatus ts = transactionManager.getTransaction(new DefaultTransactionDefinition());
		try {
			userId = userDAO.insertUser(user);
			UserRoles userRoles = new UserRoles();
			userRoles.setUserId(userId);
			userRoles.setRoles(userRegistrationDetails.getRoles());
			if (userRegistrationDetails.getRoles() != null) {
				for (String role : userRegistrationDetails.getRoles()) {
					userRoleDAO.insertUserRole(userId, role);
				}
			}
			transactionManager.commit(ts);
		} catch (Exception e) {
			transactionManager.rollback(ts);
			throw new RuntimeException(e);
		}
		return userId;
	}

	@Override
	public User getUserByUserId(Long userId) {
		UserEntity userEntity = userDAO.getUserByUserId(userId);
		// System.out.println("Password --------------------- >>> "+
		// userEntity.getPassword());
		List<String> roles = this.getUserRoleByUserId(userEntity.getId());
		User user = new User();
		this.mapUserEntityToUser(userEntity, user);
		user.setRoles(roles);
		return user;
	}

	@Override
	public User getUserByUserName(String userName) {
		UserEntity userEntity = userDAO.getUserByUserName(userName);
		// System.out.println("Password --------------------- >>> "+
		// userEntity.getPassword());
		List<String> roles = this.getUserRoleByUserId(userEntity.getId());
		User user = new User();
		this.mapUserEntityToUser(userEntity, user);
		user.setRoles(roles);
		return user;
	}

	public User mapUserEntityToUser(UserEntity userEntity, User user) {
		user.setId(userEntity.getId());
		user.setUserName(userEntity.getUserName());
		user.setEmail(userEntity.getEmail());
		user.setPassword(userEntity.getPassword());
		user.setEnabled(userEntity.isEnabled());
		user.setUserDisplayName(userEntity.getUserDisplayName());
		user.setBusinessUnit(userEntity.getBusinessUnit());
		return user;
	}

	public UserRes mapUserEntityToUserRes(UserEntity userEntity, UserRes user) {
		user.setUserId(userEntity.getId());
		user.setUserName(userEntity.getUserName());
		user.setEmail(userEntity.getEmail());
		user.setEnabled(userEntity.isEnabled());
		user.setUserDisplayName(userEntity.getUserDisplayName());
		user.setBusinessUnit(userEntity.getBusinessUnit());
		return user;
	}

	public UserRes mapUserEntityToUserResSummary(UserEntity userEntity, UserRes user) {
		user.setUserId(userEntity.getId());
		user.setUserName(userEntity.getUserName());
		user.setEmail(userEntity.getEmail());
		user.setUserDisplayName(userEntity.getUserDisplayName());
		user.setBusinessUnit(userEntity.getBusinessUnit());
		return user;
	}

	// Implement with DAO
	public List<String> getUserRoleByUserId(Long userId) {
		List<UserRoleEntity> roleEntities = userRoleDAO.getUserRolesByUserId(userId);
		List<String> roles = new ArrayList<String>();
		if (roleEntities != null) {
			for (UserRoleEntity role : roleEntities) {
				roles.add(role.getRole());
			}
		}
		return roles;
	}

	@Override
	public void replaceRoles(UserRoles userRoles) {
		userRoleDAO.replaceRoles(userRoles.getUserId(), userRoles.getRoles());

	}

	@Override
	public ForgotPasswordResponse forgotPassword(UserRegistrationDetails userRegistrationDetails) {
		ForgotPasswordResponse forgotPasswordResponse = new ForgotPasswordResponse();
		FilterUserRes filterUserRes = new FilterUserRes();
		filterUserRes.setEmail(userRegistrationDetails.getEmail());
		filterUserRes.setUserName(userRegistrationDetails.getUserName());
		UserRes user = null;
		List<UserRes> users = this.filterUsers(filterUserRes);
		if (users != null && !users.isEmpty() && (users.size() == 1) && (users.get(0) != null)) {
			user = users.get(0);
			forgotPasswordResponse.setEmail(user.getEmail());
			forgotPasswordResponse.setUserName(user.getUserName());
			forgotPasswordResponse.setUserDisplayName(user.getUserDisplayName());
			// forgotPasswordResponse.setForgotPasswordUri("https://testuri");
			forgotPasswordResponse.setValidUser(true);
			String tempPassword = PasswordGenerationUtil.getPassword();
			this.disableUser(users.get(0).getUserId(), tempPassword);
			sendForgotMail(mailUserName, user.getEmail(), tempPassword);
		} else {
			forgotPasswordResponse.setValidUser(false);
		}
		return forgotPasswordResponse;
	}

	/*
	 * public List<UserRole> getUserRolesByUserName(String userName) {
	 * List<UserRole> roles = new ArrayList<UserRole>(); roles.add("ADMIN"); return
	 * roles; }
	 */

	private void sendForgotMail(String mailFrom, String mailTo, String tempPassword) {
		List<String> mailToLst = new ArrayList<String>();
		mailToLst.add(mailTo);
		mailService.sendMail(mailFrom, mailToLst, "Forgot password", String.format("Your temporary password %s is sent on your Email %s",tempPassword,mailTo));
		/*
		 * final String username = "shiv.orian@gmail.com"; final String password =
		 * "d1ngd0ng";
		 * 
		 * Properties prop = new Properties(); prop.put("mail.smtp.host",
		 * "smtp.gmail.com"); prop.put("mail.smtp.port", "465");
		 * prop.put("mail.smtp.auth", "true"); prop.put("mail.smtp.socketFactory.port",
		 * "465"); prop.put("mail.smtp.socketFactory.class",
		 * "javax.net.ssl.SSLSocketFactory");
		 * 
		 * Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
		 * protected PasswordAuthentication getPasswordAuthentication() { return new
		 * PasswordAuthentication(username, password); } });
		 * 
		 * try {
		 * 
		 * Message message = new MimeMessage(session); message.setFrom(new
		 * InternetAddress("shiv.orian@gmail.com"));
		 * message.setRecipients(Message.RecipientType.TO, InternetAddress.
		 * parse("shiv.orian@gmail.com, shivanshu.yadav@amdocs.com, deviator206@gmail.com"
		 * )); message.setSubject("Testing Gmail SSL from Java");
		 * message.setText("Dear Mail Crawler," + "\n\n Please do not spam my email!");
		 * 
		 * Transport.send(message);
		 * 
		 * System.out.println("Done");
		 * 
		 * } catch (MessagingException e) { e.printStackTrace(); }
		 */
	}

	@Override
	public void updateUserByUserId(UserRegistrationDetails userRegistrationDetails) {
		UserEntity user = new UserEntity();
		user.setUserName(userRegistrationDetails.getUserName());
		user.setId(userRegistrationDetails.getUserId());
		user.setUserDisplayName(userRegistrationDetails.getUserDisplayName());
		user.setBusinessUnit(userRegistrationDetails.getBusinessUnit());
		user.setEmail(userRegistrationDetails.getEmail());
		userDAO.updateUser(user);
	}

	@Override
	public void changePasswordByUserId(UserRegistrationDetails userRegistrationDetails) {
		UserEntity user = new UserEntity();
		user.setId(userRegistrationDetails.getUserId());
		// Enabled false while adding user
		user.setEnabled(true);
		user.setPassword(passwordEncoder.encode(userRegistrationDetails.getPassword()));
		userDAO.updateUser(user);
	}

	@Override
	public List<UserRes> filterUsers(FilterUserRes filterUserRes) {
		List<UserRes> users = new ArrayList<UserRes>();
		if (filterUserRes.isValidFilter()) {
			List<UserEntity> userEntities = userDAO.filterUsers(filterUserRes);
			for (UserEntity userEntity : userEntities) {
				UserRes user = new UserRes();
				ModelEntityMappers.mapUserEntityToUserRes(userEntity, user);
				List<String> roles = this.getUserRoleByUserId(userEntity.getId());
				user.setRoles(roles);
				users.add(user);
			}

			return users;
		}

		return users;
	}

	@Override
	public void disableUser(Long userId, String tempPassword) {
		UserEntity user = new UserEntity();
		user.setEnabled(false);
		user.setId(userId);
		user.setPassword(passwordEncoder.encode(tempPassword));
		userDAO.disableUser(user);
	}

}
