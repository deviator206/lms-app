package service;

import model.LeadRes;
import model.RootLeadRes;

public interface ILeadDetailService {
	Long createRootLead(RootLeadRes rootLeadRes);

	RootLeadRes getRootLead(Long id);

	LeadRes getLead(Long id);

	boolean updateLead(LeadRes leadRes);

}
