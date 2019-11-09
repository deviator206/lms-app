package service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import model.CreateRootLeadRes;
import model.DownloadFileRes;
import model.FilterLeadRes;
import model.LeadRes;
import model.LeadStatistictsRes;
import model.Pagination;
import model.RootLeadRes;
import model.UploadFileRes;

public interface ILeadDetailService {
	CreateRootLeadRes createRootLead(RootLeadRes rootLeadRes);

	RootLeadRes getRootLead(Long id);

	LeadRes getLead(Long id);

	List<LeadRes> getLeads(String leadtype, Long userId, Pagination pagination);

	List<LeadRes> getLeadsByRoot(Long rootLeadId);

	List<LeadRes> searchLeads(String name, String description);

	Long updateLead(LeadRes leadRes);

	Long updateLeadAttachment(Long leadId, String path);

	List<LeadRes> filterLeads(FilterLeadRes filterLeadRes, Pagination pagination);

	LeadStatistictsRes getLeadStatistics(FilterLeadRes filterLeadRes, Boolean sendmail, Long userId);

	ByteArrayInputStream getLeadStatisticsReport(FilterLeadRes filterLeadRes, Boolean busummary, Long userId)
			throws IOException;

	UploadFileRes uploadLeadAttachment(Long id, Long userId, List<MultipartFile> files) throws IOException;

	UploadFileRes uploadLeadAttachments(List<Long> leadIds, Long userId, List<MultipartFile> files) throws IOException;

	DownloadFileRes downloadLeadAttachment(Long leadId, String name) throws IOException;

	UploadFileRes uploadRootLeadContactAttachment(Long id, List<MultipartFile> files) throws IOException;
	public DownloadFileRes downloadContactAttachment(Long leadId, String name) throws IOException;

}
