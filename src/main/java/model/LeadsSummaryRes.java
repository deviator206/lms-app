package model;

import java.util.List;

public class LeadsSummaryRes {
	private List<String> businessUnits;
	private String rootLeadId;
	private String salesRep;
	private String industry;
	
	public List<String> getBusinessUnits() {
		return businessUnits;
	}
	public void setBusinessUnits(List<String> businessUnits) {
		this.businessUnits = businessUnits;
	}
	public String getRootLeadId() {
		return rootLeadId;
	}
	public void setRootLeadId(String rootLeadId) {
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
