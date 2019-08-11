package repository;

import java.util.List;

import repository.entity.LeadEntity;

public interface ILeadDAO {
	LeadEntity getLead(Long id);

	List<LeadEntity> getLeads();

	List<LeadEntity> searchLeads(String name, String description);

	Long insertLead(LeadEntity leadEntity);

	boolean deleteLead(Long id);

	boolean updateLead(LeadEntity leadEntity);
}
