package service;

import java.util.List;

public interface IMailService {
	public void sendMail(String mailFrom, List<String> mailTo, String subject,boolean htmlContent, String body);

	public void sendMailAsyn(String mailFrom, List<String> mailTo, String subject,boolean htmlContent, String body);
}
