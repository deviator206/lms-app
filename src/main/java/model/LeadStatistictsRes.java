package model;

import java.util.Map;

public class LeadStatistictsRes {
	Map<String, Long> leadStatusCountMap;

	public Map<String, Long> getLeadStatusCountMap() {
		return leadStatusCountMap;
	}

	public void setLeadStatusCountMap(Map<String, Long> leadStatusCountMap) {
		this.leadStatusCountMap = leadStatusCountMap;
	}
	
	private Long internalBuLeadsCount;
	private Long externalBuLeadsCount;
	private Long crossBuLeadsCount;

	public Long getExternalBuLeadsCount() {
		return externalBuLeadsCount;
	}

	public void setExternalBuLeadsCount(Long externalBuLeadsCount) {
		this.externalBuLeadsCount = externalBuLeadsCount;
	}

	public Long getCrossBuLeadsCount() {
		return crossBuLeadsCount;
	}

	public void setCrossBuLeadsCount(Long crossBuLeadsCount) {
		this.crossBuLeadsCount = crossBuLeadsCount;
	}

	public Long getInternalBuLeadsCount() {
		return internalBuLeadsCount;
	}

	public void setInternalBuLeadsCount(Long internalBuLeadsCount) {
		this.internalBuLeadsCount = internalBuLeadsCount;
	}
}
