package service;

import java.util.List;

import model.FilterMarketIntelligenceRes;
import model.MarketIntelligenceInfoRes;
import model.MarketIntelligenceRes;
import repository.entity.MarketIntelligenceInfoEntity;

public interface IMarketIntelligenceService {
	List<MarketIntelligenceRes> getMarketIntelligence();

	MarketIntelligenceRes getMarkByIdetIntelligenceById(Long id);

	Long updateMarketIntelligence(MarketIntelligenceRes marketIntelligenceRes);

	Long addMarketIntelligence(MarketIntelligenceRes marketIntelligenceRes);

	public List<MarketIntelligenceInfoRes> getMarketIntelligenceInfo(Long miId);

	void addMarketIntelligenceInfo(Long miId, MarketIntelligenceInfoEntity miInfoEntity);

	List<MarketIntelligenceRes> filterMarketIntelligence(FilterMarketIntelligenceRes filterMarketIntelligence);

}
