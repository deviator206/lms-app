package model;

import java.util.Date;

import security.SqlSafeUtil;

public class FilterMarketIntelligenceRes {
	private String type;
	private String name;
	private Double investment;
	private Date startDate;
	private Date endDate;
	private String searchText;

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getInvestment() {
		return investment;
	}

	public void setInvestment(Double investment) {
		this.investment = investment;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Boolean isSqlInjectionSafe() {
		if (SqlSafeUtil.isSqlInjectionSafe(this.getType()) && SqlSafeUtil.isSqlInjectionSafe(this.getName())
				&& SqlSafeUtil.isSqlInjectionSafe(this.getSearchText())) {
			return true;
		}
		return false;
	}
}
