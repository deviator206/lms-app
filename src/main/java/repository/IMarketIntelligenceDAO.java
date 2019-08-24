package repository;

import java.util.List;

import model.FilterMarketIntelligenceRes;
import repository.entity.MarketIntelligenceEntity;
import repository.entity.MarketIntelligenceInfoEntity;

public interface IMarketIntelligenceDAO {
	List<MarketIntelligenceEntity> getMarketIntelligence();

	MarketIntelligenceEntity getMarkByIdetIntelligenceById(Long id);

	void updateMarketIntelligence(MarketIntelligenceEntity marketingIntelligenceEntity);

	void updateLeadInMarketIntelligence(Long rootLeadId, Long miId);

	Long addMarketIntelligence(MarketIntelligenceEntity marketingIntelligenceEntity);

	List<MarketIntelligenceInfoEntity> getMarketIntelligenceInfoList(Long miId);

	void addMarketIntelligenceInfo(Long miId, String info);
	
	List<MarketIntelligenceEntity> filterMarketIntelligence(FilterMarketIntelligenceRes filterMarketIntelligence);

}
