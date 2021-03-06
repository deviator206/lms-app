package model;

import java.sql.Date;

public class LeadRes {
	private Long id;
	private String source;
	private String custName;
	private String description;
	private LeadContactRes leadContact;
	private LeadsSummaryRes leadsSummaryRes;
	private Date updateDate;
	private Long updatorId;
	private Date creationDate;
	private long inactiveDuration;
	private String tenure;
	private String status;
	private Long creatorId;
	private UserRes creator;
	private String attachment;
	private String sourceInfo;
	public String getSourceInfo() {
		return sourceInfo;
	}

	public void setSourceInfo(String sourceInfo) {
		this.sourceInfo = sourceInfo;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public UserRes getCreator() {
		return creator;
	}

	public void setCreator(UserRes creator) {
		this.creator = creator;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTenure() {
		return tenure;
	}

	public void setTenure(String tenure) {
		this.tenure = tenure;
	}

	public long getInactiveDuration() {
		return inactiveDuration;
	}

	public void setInactiveDuration(long inactiveDuration) {
		this.inactiveDuration = inactiveDuration;
	}

	private boolean deleted;

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LeadsSummaryRes getLeadsSummaryRes() {
		return leadsSummaryRes;
	}

	public void setLeadsSummaryRes(LeadsSummaryRes leadsSummaryRes) {
		this.leadsSummaryRes = leadsSummaryRes;
	}

	public LeadContactRes getLeadContact() {
		return leadContact;
	}

	public void setLeadContact(LeadContactRes leadContact) {
		this.leadContact = leadContact;
	}

	// TODO : Check whether name or Id?
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String deascription) {
		this.description = deascription;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public Long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Long getUpdatorId() {
		return updatorId;
	}

	public void setUpdatorId(Long updatorId) {
		this.updatorId = updatorId;
	}
}
