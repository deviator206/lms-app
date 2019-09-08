package repository;

import java.util.Date;
import java.util.List;

import model.FilterMarketIntelligenceRes;
import model.Pagination;
import repository.entity.LeadEntity;
import repository.entity.MarketIntelligenceEntity;
import repository.entity.MarketIntelligenceInfoEntity;

public interface IMarketIntelligenceDAO {
	List<MarketIntelligenceEntity> getMarketIntelligence(Pagination pagination);

	MarketIntelligenceEntity getMarkByIdetIntelligenceById(Long id);

	void updateMarketIntelligence(MarketIntelligenceEntity marketingIntelligenceEntity);

	void updateLeadInMarketIntelligence(Long miId, Long rootLeadId, String status, Long updatorId,Date updateDate);

	Long addMarketIntelligence(MarketIntelligenceEntity marketingIntelligenceEntity);

	List<MarketIntelligenceInfoEntity> getMarketIntelligenceInfoList(Long miId);

	void addMarketIntelligenceInfo(Long miId, MarketIntelligenceInfoEntity miEntity);
	
	List<MarketIntelligenceEntity> filterMarketIntelligence(FilterMarketIntelligenceRes filterMarketIntelligence,Pagination pagination);
	
	void updateMarketIntelligenceAttachment(LeadEntity leadEntity);

}
