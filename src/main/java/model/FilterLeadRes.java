package model;

import java.util.Date;

public class FilterLeadRes {
	private String custName;
	private String salesRep;
	private Long salesRepId;

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
