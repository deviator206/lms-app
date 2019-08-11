package service;

import java.util.List;

import model.LeadRes;
import model.RootLeadRes;

public interface ILeadDetailService {
	Long createRootLead(RootLeadRes rootLeadRes);

	RootLeadRes getRootLead(Long id);

	LeadRes getLead(Long id);

	List<LeadRes> getLeads();

	List<LeadRes> searchLeads(String name, String description);

	Long updateLead(LeadRes leadRes);

}
