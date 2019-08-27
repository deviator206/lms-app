package service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import consts.LeadManagementConstants;
import model.FilterMarketIntelligenceRes;
import model.MarketIntelligenceInfoRes;
import model.MarketIntelligenceRes;
import repository.IMarketIntelligenceDAO;
import repository.entity.MarketIntelligenceEntity;
import repository.entity.MarketIntelligenceInfoEntity;
import repository.mapper.ModelEntityMappers;

@Service
public class MarketIntelligenceService implements IMarketIntelligenceService {
	@Autowired
	private IMarketIntelligenceDAO mrketIntelligenceDAO;

	@Autowired
	private ILeadDetailService leadDetailService;

	@Autowired
	private PlatformTransactionManager transactionManager;

	@Override
	public List<MarketIntelligenceRes> getMarketIntelligence() {
		List<MarketIntelligenceEntity> miList = mrketIntelligenceDAO.getMarketIntelligence();
		List<MarketIntelligenceRes> miResList = new ArrayList<MarketIntelligenceRes>();
		MarketIntelligenceRes miRes;
		for (MarketIntelligenceEntity miEntity : miList) {
			miRes = new MarketIntelligenceRes();
			ModelEntityMappers.mapMiEntityToMiRes(miEntity, miRes);
			miResList.add(miRes);
		}
		return miResList;
	}

	@Override
	public MarketIntelligenceRes getMarkByIdetIntelligenceById(Long id) {
		MarketIntelligenceEntity miEntity = mrketIntelligenceDAO.getMarkByIdetIntelligenceById(id);
		MarketIntelligenceRes miRes = new MarketIntelligenceRes();
		ModelEntityMappers.mapMiEntityToMiRes(miEntity, miRes);
		List<MarketIntelligenceInfoRes> miInfoResLst = this.getMarketIntelligenceInfo(miRes.getId());
		miRes.setMiInfoList(miInfoResLst);
		return miRes;
	}

	@Override
	public Long updateMarketIntelligence(MarketIntelligenceRes marketIntelligenceRes) {
		TransactionStatus ts = transactionManager.getTransaction(new DefaultTransactionDefinition());
		try {
			if (marketIntelligenceRes.getMiInfoList() != null && !marketIntelligenceRes.getMiInfoList().isEmpty()) {
				MarketIntelligenceInfoEntity miInfoEntity = new MarketIntelligenceInfoEntity();
				ModelEntityMappers.mapMiInfoResToMiInfoEntity(marketIntelligenceRes.getMiInfoList().get(0),
						miInfoEntity);

				// Set Creation Date
				Date currentDate = new Date();
				miInfoEntity.setCreationDate(currentDate);
				miInfoEntity.setCreatorId(marketIntelligenceRes.getUpdatorId());

				this.addMarketIntelligenceInfo(marketIntelligenceRes.getId(), miInfoEntity);
			}

			Date updateDate = marketIntelligenceRes.getUpdateDate() != null ? marketIntelligenceRes.getUpdateDate()
					: new Date();

			if (marketIntelligenceRes.getRootLeadId() != null) {
				mrketIntelligenceDAO.updateLeadInMarketIntelligence(marketIntelligenceRes.getId(),
						marketIntelligenceRes.getRootLeadId(), LeadManagementConstants.MI_STATUS_CLOSED,
						marketIntelligenceRes.getUpdatorId(), updateDate);
			}

			/*
			 * if (marketIntelligenceRes.getRootLead() != null) { Long rootLeadId =
			 * leadDetailService.createRootLead(marketIntelligenceRes.getRootLead());
			 * mrketIntelligenceDAO.updateLeadInMarketIntelligence(rootLeadId,
			 * marketIntelligenceRes.getId()); }
			 */
			transactionManager.commit(ts);
		} catch (Exception e) {
			transactionManager.rollback(ts);
			throw new RuntimeException(e);
		}
		return marketIntelligenceRes.getId();
	}

	@Override
	public Long addMarketIntelligence(MarketIntelligenceRes marketIntelligenceRes) {
		MarketIntelligenceEntity miEntity = new MarketIntelligenceEntity();
		ModelEntityMappers.mapMiResToMiEntity(marketIntelligenceRes, miEntity);
		return mrketIntelligenceDAO.addMarketIntelligence(miEntity);
	}

	@Override
	public List<MarketIntelligenceInfoRes> getMarketIntelligenceInfo(Long miId) {
		List<MarketIntelligenceInfoEntity> miInfoList = mrketIntelligenceDAO.getMarketIntelligenceInfoList(miId);
		List<MarketIntelligenceInfoRes> miInfoResList = new ArrayList<MarketIntelligenceInfoRes>();
		MarketIntelligenceInfoRes miInfoRes;
		for (MarketIntelligenceInfoEntity miInfoEntity : miInfoList) {
			miInfoRes = new MarketIntelligenceInfoRes();
			ModelEntityMappers.mapMiInfoEntityToMiInfoRes(miInfoEntity, miInfoRes);
			miInfoResList.add(miInfoRes);
		}
		return miInfoResList;
	}

	@Override
	public void addMarketIntelligenceInfo(Long miId, MarketIntelligenceInfoEntity miInfoEntity) {
		mrketIntelligenceDAO.addMarketIntelligenceInfo(miId, miInfoEntity);
	}

	@Override
	public List<MarketIntelligenceRes> filterMarketIntelligence(FilterMarketIntelligenceRes filterMarketIntelligence) {
		List<MarketIntelligenceEntity> miList = mrketIntelligenceDAO.filterMarketIntelligence(filterMarketIntelligence);
		List<MarketIntelligenceRes> miResList = new ArrayList<MarketIntelligenceRes>();
		MarketIntelligenceRes miRes;
		for (MarketIntelligenceEntity miEntity : miList) {
			miRes = new MarketIntelligenceRes();
			ModelEntityMappers.mapMiEntityToMiRes(miEntity, miRes);
			miResList.add(miRes);
		}
		return miResList;
	}
}
