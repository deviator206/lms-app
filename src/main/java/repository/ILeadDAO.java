package repository;

import java.util.List;

import model.FilterLeadRes;
import repository.entity.LeadEntity;

public interface ILeadDAO {
	LeadEntity getLead(Long id);

	List<LeadEntity> getLeads();

	List<LeadEntity> searchLeads(String name, String description);

	Long insertLead(LeadEntity leadEntity);

	boolean deleteLead(Long id);

	boolean updateLead(LeadEntity leadEntity);
	
	List<LeadEntity> filterLeads(FilterLeadRes filterLeadRes);
}
