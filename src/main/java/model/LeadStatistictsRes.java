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
}
