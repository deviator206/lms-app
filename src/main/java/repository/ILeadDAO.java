package repository;

import repository.entity.LeadEntity;

public interface ILeadDAO {
	LeadEntity getLead(Long id);

	Long insertLead(LeadEntity leadEntity);

	boolean deleteLead(Long id);
	
	boolean updateLead(LeadEntity leadEntity);
}
