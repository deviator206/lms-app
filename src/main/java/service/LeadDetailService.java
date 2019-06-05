package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import consts.LeadManagementConstants;
import model.LeadContactRes;
import model.LeadRes;
import model.RootLeadRes;
import repository.ILeadContactDAO;
import repository.ILeadDAO;
import repository.IRootLeadDAO;
import repository.entity.LeadContactEntity;
import repository.entity.LeadEntity;
import repository.entity.RootLeadEntity;
import repository.mapper.ModelEntityMappers;

@Service
public class LeadDetailService implements ILeadDetailService {
	@Autowired
	private IRootLeadDAO rootLeadDAO;

	@Autowired
	private ILeadContactDAO leadContactDAO;

	@Autowired
	private ILeadDAO leadDAO;

	@Autowired
	private PlatformTransactionManager transactionManager;

	@Override
	public Long createRootLead(RootLeadRes rootLeadRes) {
		Long rootLeadId;
		TransactionStatus ts = transactionManager.getTransaction(new DefaultTransactionDefinition());
		try {
			LeadContactEntity leadContactEntity = new LeadContactEntity();
			ModelEntityMappers.mapLeadContactResToLeadContactEntity(rootLeadRes.getLeadContact(), leadContactEntity);
			Long leadContactId = leadContactDAO.insertLeadContact(leadContactEntity);
			RootLeadEntity rootLeadEntity = new RootLeadEntity();

			ModelEntityMappers.mapRootLeadResToRootLeadEntity(rootLeadRes, rootLeadEntity);
			rootLeadEntity.setContactId(leadContactId);
			rootLeadId = rootLeadDAO.insertRootLead(rootLeadEntity);
			if (rootLeadRes.getLeadsSummaryRes() != null) {
				for (String businessUnit : rootLeadRes.getLeadsSummaryRes().getBusinessUnits()) {
					LeadEntity leadEntity = new LeadEntity();
					leadEntity.setRootLeadId(rootLeadId);
					leadEntity.setBusinessUnit(businessUnit);
					leadEntity.setIndustry(rootLeadRes.getLeadsSummaryRes().getIndustry());
					leadEntity.setSalesRep(rootLeadRes.getLeadsSummaryRes().getSalesRep());
					leadEntity.setCreatorId(rootLeadRes.getCreatorId());
					leadEntity.setCreationDate(rootLeadRes.getCreationDate());
					leadEntity.setUpdatorId(rootLeadRes.getCreatorId());
					leadEntity.setUpdateDate(rootLeadRes.getCreationDate());
					leadEntity.setStatus(LeadManagementConstants.LEAD_STATUS_DRAFT);
					leadDAO.insertLead(leadEntity);
				}
			}
			transactionManager.commit(ts);
		} catch (Exception e) {
			transactionManager.rollback(ts);
			throw new RuntimeException(e);
		}

		return rootLeadId;
	}

	@Override
	public RootLeadRes getRootLead(Long id) {
		RootLeadEntity rootLeadEntity = rootLeadDAO.getRootLead(id);
		RootLeadRes rootLeadRes = new RootLeadRes();
		rootLeadRes = ModelEntityMappers.mapRootLeadEntityToRootLeadRes(rootLeadEntity, rootLeadRes);
		LeadContactEntity leadContactEntity = leadContactDAO.getLeadContact(rootLeadEntity.getContactId());
		LeadContactRes leadContactRes = new LeadContactRes();
		ModelEntityMappers.mapLeadContactEntityToLeadContactRes(leadContactEntity, leadContactRes);
		rootLeadRes.setLeadContact(leadContactRes);
		// TODO : Map Summary of BU
		return rootLeadRes;
	}

	@Override
	public LeadRes getLead(Long id) {
		LeadEntity leadEntity = leadDAO.getLead(id);
		LeadRes leadRes = new LeadRes();
		ModelEntityMappers.mapLeadEntityToLeadRes(leadEntity, leadRes);
		return leadRes;
	}

	@Override
	public boolean updateLead(LeadRes leadRes) {
		LeadEntity leadEntity = new LeadEntity();
		ModelEntityMappers.mapLeadResoLeadEntity(leadRes, leadEntity);
		return leadDAO.updateLead(leadEntity);
	}

}
