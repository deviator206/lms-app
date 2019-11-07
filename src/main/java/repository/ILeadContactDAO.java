package repository;

import java.sql.SQLException;

import repository.entity.LeadContactEntity;

public interface ILeadContactDAO {
	LeadContactEntity getLeadContact(Long id);

	Long insertLeadContact(LeadContactEntity leadContactEntity);
	public boolean updateLeadContact(LeadContactEntity leadContactEntity);

	boolean deleteLeadContact(Long id);
}
