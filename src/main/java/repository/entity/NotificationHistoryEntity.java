package repository.entity;

import java.util.Date;

public class NotificationHistoryEntity {
	private Long id;
	private Long recipientId;
	private Long originatorId;
	private Boolean deleted;
	private String notificationText;
	private String notificationType;
	
	public String getNotificationType() {
		return notificationType;
	}
	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}
	public String getNotificationText() {
		return notificationText;
	}
	public void setNotificationText(String notificationText) {
		this.notificationText = notificationText;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getRecipientId() {
		return recipientId;
	}
	public void setRecipientId(Long recipientId) {
		this.recipientId = recipientId;
	}
	public Long getOriginatorId() {
		return originatorId;
	}
	public void setOriginatorId(Long originatorId) {
		this.originatorId = originatorId;
	}
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	public Date getOriginationDate() {
		return originationDate;
	}
	public void setOriginationDate(Date originationDate) {
		this.originationDate = originationDate;
	}
	private Date originationDate;
}
