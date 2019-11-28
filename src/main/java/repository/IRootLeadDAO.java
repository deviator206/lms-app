package repository;

import repository.entity.RootLeadEntity;

public interface IRootLeadDAO {
	RootLeadEntity getRootLead(Long id);
	public void updateRootLead(RootLeadEntity rootLeadEntity);

	Long insertRootLead(RootLeadEntity rootLeadEntity);

	boolean deleteRootLead(Long id);
}
