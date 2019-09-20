package service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import model.DownloadFileRes;
import model.FilterMarketIntelligenceRes;
import model.MarketIntelligenceInfoRes;
import model.MarketIntelligenceReq;
import model.MarketIntelligenceRes;
import model.Pagination;
import model.UploadFileRes;
import repository.entity.MarketIntelligenceInfoEntity;

public interface IMarketIntelligenceService {
	List<MarketIntelligenceRes> getMarketIntelligence(Pagination pagination);

	MarketIntelligenceRes getMarkByIdetIntelligenceById(Long id, Pagination pagination);

	Long updateMarketIntelligence(MarketIntelligenceReq marketIntelligenceRes);

	Long addMarketIntelligence(MarketIntelligenceReq marketIntelligenceRes);

	public List<MarketIntelligenceInfoRes> getMarketIntelligenceInfo(Long miId,Pagination pagination);

	void addMarketIntelligenceInfo(Long miId, MarketIntelligenceInfoEntity miInfoEntity);

	List<MarketIntelligenceRes> filterMarketIntelligence(FilterMarketIntelligenceRes filterMarketIntelligence,
			Pagination pagination);

	UploadFileRes uploadMiAttachment(Long id, Long userId, List<MultipartFile> files) throws IOException;

	DownloadFileRes downloadMiAttachment(Long miId, String name) throws IOException;

}
