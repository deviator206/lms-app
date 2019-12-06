package service;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import util.ExcelCreator;

@Service
@Scope("prototype")
public class MailService implements IMailService {
	private static final String MESSAGE_EMAIL_SENDING_ERROR = "EMAIL SENDING ERROR";

	private static final String MAIL_SMTP_SSL_CHECKSERVERIDENTITY = "mail.smtp.ssl.checkserveridentity";

	private static final String REPORTS_XLS = "reports.xls";

	private static final String TEXT_HTML = "text/html";

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final String JAVAX_NET_SSL_SSL_SOCKET_FACTORY = "javax.net.ssl.SSLSocketFactory";

	private static final String MAIL_SMTP_SOCKET_FACTORY_CLASS = "mail.smtp.socketFactory.class";

	private static final String MAIL_SMTP_SOCKET_FACTORY_PORT = "mail.smtp.socketFactory.port";

	private static final String MAIL_SMTP_AUTH = "mail.smtp.auth";

	private static final String MAIL_SMTP_PORT = "mail.smtp.port";

	private static final String MAIL_SMTP_HOST = "mail.smtp.host";

	private static final int NO_OF_QUICK_SERVICE_THREADS = 20;

	/**
	 * this statement create a thread pool of twenty threads here we are assigning
	 * send mail task using ScheduledExecutorService.submit();
	 */
	private ScheduledExecutorService quickService = Executors.newScheduledThreadPool(NO_OF_QUICK_SERVICE_THREADS);

	@Value("${app.mail.userName}")
	private String mailUserName;

	@Value("${app.mail.password}")
	private String password;

	@Value("${app.mail.smtp.host}")
	private String smtpHost;

	@Value("${app.mail.smtp.port}")
	private String smtpPort;

	@Value("${app.mail.smtp.auth}")
	private Boolean smtpAuth;

	@Autowired
	private IUtilityService utilityService;

	@Override
	public void sendMail(String mailFrom, List<String> mailTo, String subject, boolean htmlContent, String body) {
		final String username = this.mailUserName;
		final String passwd = utilityService.decrypt(this.password);

		Properties prop = new Properties();
		prop.put(MAIL_SMTP_HOST, smtpHost);
		prop.put(MAIL_SMTP_PORT, smtpPort);
		prop.put(MAIL_SMTP_AUTH, smtpAuth);
		prop.put(MAIL_SMTP_SOCKET_FACTORY_PORT, smtpPort);
		prop.put(MAIL_SMTP_SOCKET_FACTORY_CLASS, JAVAX_NET_SSL_SSL_SOCKET_FACTORY);
		prop.put(MAIL_SMTP_SSL_CHECKSERVERIDENTITY, true); // Compliant

		Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, passwd);
			}
		});

		try {
			Message message = new MimeMessage(session);
			if (mailFrom != null) {
				message.setFrom(new InternetAddress(mailFrom));
			} else {
				message.setFrom(new InternetAddress(mailUserName));
			}

			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(String.join(",", mailTo)));
			message.setSubject(subject);

			if (htmlContent) {
				message.setContent(body, TEXT_HTML);
			} else {
				message.setText(body);
			}

			Transport.send(message);

		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendMailAsyn(String mailFrom, List<String> mailTo, String subject, boolean htmlContent, String body) {
		final String username = this.mailUserName;
		final String password = utilityService.decrypt(this.password);

		Properties prop = new Properties();
		prop.put(MAIL_SMTP_HOST, smtpHost);
		prop.put(MAIL_SMTP_PORT, smtpPort);
		prop.put(MAIL_SMTP_AUTH, smtpAuth);
		prop.put(MAIL_SMTP_SOCKET_FACTORY_PORT, smtpPort);
		prop.put(MAIL_SMTP_SOCKET_FACTORY_CLASS, JAVAX_NET_SSL_SSL_SOCKET_FACTORY);
		prop.put(MAIL_SMTP_SSL_CHECKSERVERIDENTITY, true); // Compliant

		Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			Message message = new MimeMessage(session);
			if (mailFrom != null) {
				message.setFrom(new InternetAddress(mailFrom));
			} else {
				message.setFrom(new InternetAddress(mailUserName));
			}

			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(String.join(",", mailTo)));
			message.setSubject(subject);

			if (htmlContent) {
				message.setContent(body, TEXT_HTML);
			} else {
				message.setText(body);
			}

			quickService.submit(new Runnable() {
				@Override
				public void run() {
					try {
						Transport.send(message);
					} catch (Exception e) {
						logger.error(MESSAGE_EMAIL_SENDING_ERROR, e.getMessage());
						// ("Exception occur while send a mail : ",e);
					}
				}
			});

		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void sendMailWithAttachmentAsyn(String mailFrom, List<String> mailTo, String subject, String contentType,
			HashMap<String, String> body) {
		final String username = this.mailUserName;
		final String password = utilityService.decrypt(this.password);

		Properties prop = new Properties();
		prop.put(MAIL_SMTP_HOST, smtpHost);
		prop.put(MAIL_SMTP_PORT, smtpPort);
		prop.put(MAIL_SMTP_AUTH, smtpAuth);
		prop.put(MAIL_SMTP_SOCKET_FACTORY_PORT, smtpPort);
		prop.put(MAIL_SMTP_SOCKET_FACTORY_CLASS, JAVAX_NET_SSL_SSL_SOCKET_FACTORY);
		prop.put(MAIL_SMTP_SSL_CHECKSERVERIDENTITY, true); // Compliant

		Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			Message message = new MimeMessage(session);
			if (mailFrom != null) {
				message.setFrom(new InternetAddress(mailFrom));
			} else {
				message.setFrom(new InternetAddress(mailUserName));
			}

			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(String.join(",", mailTo)));
			message.setSubject(subject);
			System.out.println("contentType :::::::::::::::::::::: " + contentType);
			switch (contentType) {

			case "ATTACHMENT":
				// Create the message part
				BodyPart messageBodyPart = new MimeBodyPart();

				// Now set the actual message
				messageBodyPart.setText(subject);
				// Create a multipar message
				Multipart multipart = new MimeMultipart();
				// Set text message part
				multipart.addBodyPart(messageBodyPart);

				// Part two is attachment
				messageBodyPart = new MimeBodyPart();
				DataSource source = ExcelCreator.getExcel(body);
				messageBodyPart.setDataHandler(new DataHandler(source));
				messageBodyPart.setFileName(REPORTS_XLS);
				multipart.addBodyPart(messageBodyPart);

				// Send the complete message parts
				message.setContent(multipart, TEXT_HTML);
				break;

			default:
				break;
			}

			quickService.submit(new Runnable() {
				@Override
				public void run() {
					try {
						Transport.send(message);
					} catch (Exception e) {
						logger.error(MESSAGE_EMAIL_SENDING_ERROR, e.getMessage());
						// ("Exception occur while send a mail : ",e);
					}
				}
			});

		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}

}
