package controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import model.NotificationHistory;
import service.INotificationService;

@RestController
public class NotificationController {
	@Autowired
	public INotificationService notificationService;

	@GetMapping("/notification")
	public List<NotificationHistory> getNotifications(@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "recipientId", required = false) Long recipientId) {
		return notificationService.getNotifications(recipientId);
	}

	@PostMapping("/notification")
	public List<Long> addNotifications(@RequestBody List<NotificationHistory> notifications) {
		return notificationService.addNotifications(notifications);
	}

	@PutMapping("/notification")
	public void updateNotifications(@RequestBody List<NotificationHistory> notifications) {
		notificationService.updateNotifications(notifications);
	}
}
