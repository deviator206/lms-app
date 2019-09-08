package model;

import java.sql.Date;

public class RootLeadRes {
	private Long id;
	private String source;
	private String sourceInfo;
	public String getSourceInfo() {
		return sourceInfo;
	}

	public void setSourceInfo(String sourceInfo) {
		this.sourceInfo = sourceInfo;
	}

	private String custName;
	private String description;
	private LeadContactRes leadContact;
	private LeadsSummaryRes leadsSummaryRes;
	private String tenure;
	
	public String getTenure() {
		return tenure;
	}

	public void setTenure(String tenure) {
		this.tenure = tenure;
	}

	private boolean selfApproved;

	public boolean isSelfApproved() {
		return selfApproved;
	}

	public void setSelfApproved(boolean selfApproved) {
		this.selfApproved = selfApproved;
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

	private boolean deleted;
	// TODO : Check whether name or Id?
	private Long creatorId;

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

	private Date creationDate;

}
