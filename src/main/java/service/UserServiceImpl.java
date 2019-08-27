package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import model.ForgotPasswordResponse;
import model.User;
import model.UserRegistrationDetails;
import model.UserRoles;
import repository.IUserDAO;
import repository.IUserRoleDAO;
import repository.entity.UserEntity;
import repository.entity.UserRoleEntity;

@Service
public class UserServiceImpl implements IUserService {
	@Autowired
	private IUserDAO userDAO;

	@Autowired
	private IUserRoleDAO userRoleDAO;

	@Resource
	private PasswordEncoder passwordEncoder;

	@Autowired
	private PlatformTransactionManager transactionManager;

	public List<User> getUsers() {
		List<UserEntity> userEntities = userDAO.getUsers();
		List<User> users = new ArrayList<User>();
		for (UserEntity userEntity : userEntities) {
			User user = new User();
			this.mapUserEntityToUser(userEntity, user);
			List<String> roles = this.getUserRoleByUserId(userEntity.getId());
			user.setRoles(roles);
			users.add(user);
		}
		return users;
	}

	@Override
	public void addUser(UserRegistrationDetails userRegistrationDetails) {
		UserEntity user = new UserEntity();
		user.setUserName(userRegistrationDetails.getUserName());
		user.setEnabled(true);
		user.setUserDisplayName(userRegistrationDetails.getUserDisplayName());
		user.setBusinessUnit(userRegistrationDetails.getBusinessUnit());
		user.setEmail(userRegistrationDetails.getEmail());
		user.setPassword(passwordEncoder.encode(userRegistrationDetails.getPassword()));

		TransactionStatus ts = transactionManager.getTransaction(new DefaultTransactionDefinition());
		try {
			Long userId = userDAO.insertUser(user);
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
		forgotPasswordResponse.setEmail(userRegistrationDetails.getEmail());
		forgotPasswordResponse.setUserName(userRegistrationDetails.getUserName());
		forgotPasswordResponse.setUserDisplayName(userRegistrationDetails.getUserDisplayName());
		sendMail();
		forgotPasswordResponse.setForgotPasswordUri("https://testuri");
		return forgotPasswordResponse;
	}

	/*
	 * public List<UserRole> getUserRolesByUserName(String userName) {
	 * List<UserRole> roles = new ArrayList<UserRole>(); roles.add("ADMIN"); return
	 * roles; }
	 */

	private void sendMail() {
		final String username = "shiv.orian@gmail.com";
        final String password = "d1ngd0ng";

        Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        
        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("shiv.orian@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse("shiv.orian@gmail.com, shivanshu.yadav@amdocs.com, deviator206@gmail.com")
            );
            message.setSubject("Testing Gmail SSL from Java");
            message.setText("Dear Mail Crawler,"
                    + "\n\n Please do not spam my email!");

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
	}

}
