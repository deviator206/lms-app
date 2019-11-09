package controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import model.NotificationHistory;
import model.Pagination;
import service.INotificationService;

@RestController
@Scope("prototype")
public class NotificationController {
	@Autowired
	public INotificationService notificationService;

	@GetMapping("/notification")
	public List<NotificationHistory> getNotifications(
			@RequestParam(value = "recipientId", required = false) Long recipientId,
			@RequestParam(value = "start", required = false) Integer start,
			@RequestParam(value = "pagesize", required = false) Integer pageSize) {
		if (start != null && pageSize != null) {
			return notificationService.getNotifications(recipientId, new Pagination(start, pageSize));
		}
		return notificationService.getNotifications(recipientId,null);
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
