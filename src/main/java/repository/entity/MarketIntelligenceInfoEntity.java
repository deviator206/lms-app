package repository.entity;

public class MarketIntelligenceInfoEntity {
	private Long id;
	private Long miId;
	private String info;
	
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
