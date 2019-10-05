package model;

import java.util.Date;

import security.SQLInjectionSafe;

public class FilterLeadRes {
	private @SQLInjectionSafe String custName;
	private String salesRep;
	private Long salesRepId;
	private String source;
	private String industry;
	private String tenure;
	private String country;
	private String state;
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getTenure() {
		return tenure;
	}

	public void setTenure(String tenure) {
		this.tenure = tenure;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	private Long creatorId;

	public Long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

	public Long getSalesRepId() {
		return salesRepId;
	}

	public void setSalesRepId(Long salesRepId) {
		this.salesRepId = salesRepId;
	}

	private Date startDate;
	private Date endDate;
	private String fromBu;
	private String toBu;

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

	public String getFromBu() {
		return fromBu;
	}

	public void setFromBu(String fromBu) {
		this.fromBu = fromBu;
	}

	public String getToBu() {
		return toBu;
	}

	public void setToBu(String toBu) {
		this.toBu = toBu;
	}

	private String status;

	public String getSalesRep() {
		return salesRep;
	}

	public void setSalesRep(String salesRep) {
		this.salesRep = salesRep;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

}
