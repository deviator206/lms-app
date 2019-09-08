package model;

import java.util.List;

public class LeadsSummaryRes {
	private List<String> businessUnits;
	private Long rootLeadId;
	private UserRes salesRep;
	public UserRes getSalesRep() {
		return salesRep;
	}

	public void setSalesRep(UserRes salesRep) {
		this.salesRep = salesRep;
	}

	private String industry;
	private Double budget;
	private String currency;
	private Long salesRepId;

	public Long getSalesRepId() {
		return salesRepId;
	}

	public void setSalesRepId(Long salesRepId) {
		this.salesRepId = salesRepId;
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

	public List<String> getBusinessUnits() {
		return businessUnits;
	}

	public void setBusinessUnits(List<String> businessUnits) {
		this.businessUnits = businessUnits;
	}

	public Long getRootLeadId() {
		return rootLeadId;
	}

	public void setRootLeadId(Long rootLeadId) {
		this.rootLeadId = rootLeadId;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}
}
