package model;

import java.util.List;

public class CreateRootLeadRes {
	private Long rootLeadId;
	private List<Long> leads;
	public Long getRootLeadId() {
		return rootLeadId;
	}
	public void setRootLeadId(Long rootLeadId) {
		this.rootLeadId = rootLeadId;
	}
	public List<Long> getLeads() {
		return leads;
	}
	public void setLeads(List<Long> leads) {
		this.leads = leads;
	}

}
