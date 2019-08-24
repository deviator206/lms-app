package service;

import java.util.List;

import model.MarketIntelligenceInfoRes;
import model.MarketIntelligenceRes;

public interface IMarketIntelligenceService {
	List<MarketIntelligenceRes> getMarketIntelligence();

	MarketIntelligenceRes getMarkByIdetIntelligenceById(Long id);

	Long updateMarketIntelligence(MarketIntelligenceRes marketIntelligenceRes);

	Long addMarketIntelligence(MarketIntelligenceRes marketIntelligenceRes);
	
	public List<MarketIntelligenceInfoRes> getMarketIntelligenceInfo(Long miId);
	
	void addMarketIntelligenceInfo(Long miId, String info);

}
