package repository;

import repository.entity.RootLeadEntity;

public interface IRootLeadDAO {
	RootLeadEntity getRootLead(Long id);

	Long insertRootLead(RootLeadEntity rootLeadEntity);

	boolean deleteRootLead(Long id);
}
