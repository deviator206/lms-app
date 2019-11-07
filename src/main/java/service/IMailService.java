package service;

import java.util.List;

public interface IMailService {
	public void sendMail(String mailFrom, List<String> mailTo, String subject, String body);
}
