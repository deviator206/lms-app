package service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.multipart.MultipartFile;

import consts.LeadManagementConstants;
import model.DownloadFileRes;
import model.FilterMarketIntelligenceRes;
import model.MarketIntelligenceInfoReq;
import model.MarketIntelligenceInfoRes;
import model.MarketIntelligenceReq;
import model.MarketIntelligenceRes;
import model.Pagination;
import model.UploadFileRes;
import model.UserRes;
import repository.IMarketIntelligenceDAO;
import repository.entity.LeadEntity;
import repository.entity.MarketIntelligenceEntity;
import repository.entity.MarketIntelligenceInfoEntity;
import repository.mapper.ModelEntityMappers;

@Service
@Scope("prototype")
public class MarketIntelligenceService implements IMarketIntelligenceService {
	@Autowired
	private IMarketIntelligenceDAO mrketIntelligenceDAO;

	@Autowired
	private PlatformTransactionManager transactionManager;

	@Autowired
	private IUserService userService;

	@Autowired
	IFileStorageService fileStorageService;

	@Override
	public List<MarketIntelligenceRes> getMarketIntelligence(Pagination pagination) {
		List<MarketIntelligenceEntity> miList = mrketIntelligenceDAO.getMarketIntelligence(pagination);
		List<MarketIntelligenceRes> miResList = new ArrayList<MarketIntelligenceRes>();
		MarketIntelligenceRes miRes;
		Map<Long, UserRes> userIdUserMap = userService.getCachedUsersSummaryMap();
		for (MarketIntelligenceEntity miEntity : miList) {
			miRes = new MarketIntelligenceRes();
			ModelEntityMappers.mapMiEntityToMiRes(miEntity, miRes);
			if (userIdUserMap != null && userIdUserMap.get(miEntity.getCreatorId()) != null) {
				miRes.setCreator(userIdUserMap.get(miEntity.getCreatorId()));
			}
			miResList.add(miRes);
		}
		return miResList;
	}

	@Override
	public MarketIntelligenceRes getMarketIntelligenceById(Long id, Pagination pagination) {
		Map<Long, UserRes> userIdUserMap;
		MarketIntelligenceEntity miEntity = mrketIntelligenceDAO.getMarkByIdetIntelligenceById(id);
		MarketIntelligenceRes miRes = new MarketIntelligenceRes();
		ModelEntityMappers.mapMiEntityToMiRes(miEntity, miRes);
		List<MarketIntelligenceInfoRes> miInfoResLst = this.getMarketIntelligenceInfo(miRes.getId(), pagination);
		userIdUserMap = userService.getCachedUsersSummaryMap();
		if (userIdUserMap != null && userIdUserMap.get(miEntity.getCreatorId()) != null) {
			miRes.setCreator(userIdUserMap.get(miEntity.getCreatorId()));
		}
		miRes.setMiInfoList(miInfoResLst);
		return miRes;
	}

	@Override
	public Long updateMarketIntelligence(MarketIntelligenceReq marketIntelligenceRes) {
		TransactionStatus ts = transactionManager.getTransaction(new DefaultTransactionDefinition());
		try {
			if (marketIntelligenceRes.getMiInfoList() != null && !marketIntelligenceRes.getMiInfoList().isEmpty()) {
				for (MarketIntelligenceInfoReq marketIntelligenceInfoReqTmp : marketIntelligenceRes.getMiInfoList()) {
					MarketIntelligenceInfoEntity miInfoEntity = new MarketIntelligenceInfoEntity();
					ModelEntityMappers.mapMiInfoReqToMiInfoEntity(marketIntelligenceInfoReqTmp,
							miInfoEntity);

					// Set Creation Date
					Date currentDate = new Date();
					Long infoCreatorId = null;
					if (marketIntelligenceRes.getUpdatorId() != null) {
						infoCreatorId = marketIntelligenceRes.getUpdatorId();
					} else if (marketIntelligenceInfoReqTmp.getCreatorId() != null) {
						infoCreatorId = marketIntelligenceInfoReqTmp.getCreatorId();
					}
					miInfoEntity.setCreatorId(infoCreatorId);
					miInfoEntity.setCreationDate(currentDate);
					this.addMarketIntelligenceInfo(marketIntelligenceRes.getId(), miInfoEntity);
				}
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
	public Long addMarketIntelligence(MarketIntelligenceReq marketIntelligenceRes) {
		MarketIntelligenceEntity miEntity = new MarketIntelligenceEntity();
		ModelEntityMappers.mapMiReqToMiEntity(marketIntelligenceRes, miEntity);
		return mrketIntelligenceDAO.addMarketIntelligence(miEntity);
	}

	@Override
	public List<MarketIntelligenceInfoRes> getMarketIntelligenceInfo(Long miId, Pagination pagination) {
		List<MarketIntelligenceInfoEntity> miInfoList = mrketIntelligenceDAO.getMarketIntelligenceInfoList(miId,
				pagination);
		List<MarketIntelligenceInfoRes> miInfoResList = new ArrayList<MarketIntelligenceInfoRes>();
		Map<Long, UserRes> userIdUserMap;
		MarketIntelligenceInfoRes miInfoRes;
		for (MarketIntelligenceInfoEntity miInfoEntity : miInfoList) {
			miInfoRes = new MarketIntelligenceInfoRes();
			ModelEntityMappers.mapMiInfoEntityToMiInfoRes(miInfoEntity, miInfoRes);

			userIdUserMap = userService.getCachedUsersSummaryMap();
			if (userIdUserMap != null && userIdUserMap.get(miInfoEntity.getCreatorId()) != null) {
				miInfoRes.setCreator(userIdUserMap.get(miInfoEntity.getCreatorId()));
				miInfoRes.setCreatorId(miInfoEntity.getCreatorId());
			}

			miInfoResList.add(miInfoRes);
		}
		return miInfoResList;
	}

	@Override
	public void addMarketIntelligenceInfo(Long miId, MarketIntelligenceInfoEntity miInfoEntity) {
		mrketIntelligenceDAO.addMarketIntelligenceInfo(miId, miInfoEntity);
	}

	@Override
	public List<MarketIntelligenceRes> filterMarketIntelligence(FilterMarketIntelligenceRes filterMarketIntelligence,
			Pagination pagination) {
		List<MarketIntelligenceEntity> miList = mrketIntelligenceDAO.filterMarketIntelligence(filterMarketIntelligence,
				pagination);
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
	public UploadFileRes uploadMiAttachment(Long id, Long userId, List<MultipartFile> files) throws IOException {
		UploadFileRes uploadFileRes = fileStorageService.uploadMiAttachment(id, files);
		LeadEntity leadEntity = new LeadEntity();
		leadEntity.setUpdatorId(userId);
		leadEntity.setUpdateDate(new java.sql.Date((new Date()).getTime()));
		leadEntity.setAttachment(uploadFileRes.getFileName());
		leadEntity.setId(id);
		mrketIntelligenceDAO.updateMarketIntelligenceAttachment(leadEntity);
		return uploadFileRes;
	}

	@Override
	public DownloadFileRes downloadMiAttachment(Long miId, String name) throws IOException {
		return fileStorageService.downloadMiAttachment(miId, name);
	}
}
