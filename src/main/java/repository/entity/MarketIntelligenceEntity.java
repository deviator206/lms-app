package repository.entity;

import java.util.Date;

public class MarketIntelligenceEntity {
	private Long id;
	private String type;
	private String name;

	private String description;
	private String status;
	private Double investment;
	private Long leadId;
	
	private Date creationDate;

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
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

	public Long getLeadId() {
		return leadId;
	}

	public void setLeadId(Long leadId) {
		this.leadId = leadId;
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
