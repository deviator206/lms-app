package model;

import java.util.Date;
import java.util.List;

public class MarketIntelligenceRes {
	private Long id;
	private String type;
	private String name;

	private String description;
	private String status;
	private Double investment;
	private Long rootLeadId;	
	private RootLeadRes rootLead;
	
	private Date creationDate;
	private Long creatorId;
	private Long updatorId;
	private Date updateDate;
	private UserRes creator;
	private String attachment;

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
	public Long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

	public Long getUpdatorId() {
		return updatorId;
	}

	public void setUpdatorId(Long updatorId) {
		this.updatorId = updatorId;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	List<MarketIntelligenceInfoRes> miInfoList;
	
	public RootLeadRes getRootLead() {
		return rootLead;
	}

	public void setRootLead(RootLeadRes rootLead) {
		this.rootLead = rootLead;
	}

	public List<MarketIntelligenceInfoRes> getMiInfoList() {
		return miInfoList;
	}

	public void setMiInfoList(List<MarketIntelligenceInfoRes> miInfoList) {
		this.miInfoList = miInfoList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public void setType(String type) {
		this.type = type;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setInvestment(Double investment) {
		this.investment = investment;
	}

	public String getType() {
		return type;
	}


	public String getDescription() {
		return description;
	}

	public Double getInvestment() {
		return investment;
	}

}
