package service;

import java.util.List;

public interface IPushNotificationService {
	public void sendNotification(String tokenId, String message);

	public void sendNotificationMulti(List<String> putIds2, String message);

}
