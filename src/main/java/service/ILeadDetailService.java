package service;

import java.util.List;

import model.FilterLeadRes;
import model.LeadRes;
import model.LeadStatistictsRes;
import model.RootLeadRes;

public interface ILeadDetailService {
	Long createRootLead(RootLeadRes rootLeadRes);

	RootLeadRes getRootLead(Long id);

	LeadRes getLead(Long id);

	List<LeadRes> getLeads(String leadtype, Long userId);

	List<LeadRes> searchLeads(String name, String description);

	Long updateLead(LeadRes leadRes);

	List<LeadRes> filterLeads(FilterLeadRes filterLeadRes);

	LeadStatistictsRes getLeadStatistics(FilterLeadRes filterLeadRes, Boolean busummary,Long userId);

}
