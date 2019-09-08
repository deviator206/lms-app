package repository;

import java.util.List;

import model.FilterLeadRes;
import model.LeadStatistictsRes;
import model.Pagination;
import repository.entity.LeadEntity;

public interface ILeadDAO {
	LeadEntity getLead(Long id);

	List<LeadEntity> getLeads(String leadtype, Long userId, Pagination pagination);

	List<LeadEntity> searchLeads(String name, String description);

	Long insertLead(LeadEntity leadEntity);

	boolean deleteLead(Long id);

	boolean updateLead(LeadEntity leadEntity);

	boolean updateLeadAttachment(LeadEntity leadEntity);

	List<LeadEntity> filterLeads(FilterLeadRes filterLeadRes, Pagination pagination);

	LeadStatistictsRes getLeadStatistics(FilterLeadRes filterLeadRes, Boolean busummary, Long userId);

}
