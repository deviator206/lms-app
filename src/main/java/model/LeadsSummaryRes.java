package model;

import java.util.List;

public class LeadsSummaryRes {
	private List<String> businessUnits;
	private Long rootLeadId;
	private String salesRep;
	private String industry;
	private Float budget;
	private String currency;

	public Float getBudget() {
		return budget;
	}

	public void setBudget(Float budget) {
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
}
