package repository.entity;

import java.sql.Date;

public class LeadEntity {
	public Long getSalesRepId() {
		return salesRepId;
	}

	public void setSalesRepId(Long salesRepId) {
		this.salesRepId = salesRepId;
	}

	private Long id;
	private String businessUnit;
	private String status;
	private String salesRep;
	private String industry;
	private Long rootLeadId;
	private boolean deleted;
	private Date creationDate;
	private Long creatorId;
	private Date updateDate;
	private Long updatorId;
	private Long salesRepId;
	private String attachment;

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	private String originatingBusinessUnit;
	
	public String getOriginatingBusinessUnit() {
		return originatingBusinessUnit;
	}

	public void setOriginatingBusinessUnit(String originatingBusinessUnit) {
		this.originatingBusinessUnit = originatingBusinessUnit;
	}

	private Double budget;
	private String currency;
	
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public Double getBudget() {
		return budget;
	}
	public void setBudget(Double budget) {
		this.budget = budget;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	
	public String getSalesRep() {
		return salesRep;
	}

	public void setSalesRep(String salesRep) {
		this.salesRep = salesRep;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBusinessUnit() {
		return businessUnit;
	}

	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getRootLeadId() {
		return rootLeadId;
	}

	public void setRootLeadId(Long rootLeadId) {
		this.rootLeadId = rootLeadId;
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
}
