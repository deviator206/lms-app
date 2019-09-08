package model;

import java.util.Date;

public class MarketIntelligenceInfoRes {
	private Long id;
	private Long miId;
	private String info;
	private Date creationDate;
	private Long creatorId;
	private UserRes creator;

	public UserRes getCreator() {
		return creator;
	}

	public void setCreator(UserRes creator) {
		this.creator = creator;
	}
	
	public Long getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getMiId() {
		return miId;
	}
	public void setMiId(Long miId) {
		this.miId = miId;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
}
