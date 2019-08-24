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
