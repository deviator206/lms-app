package service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import consts.LeadManagementConstants;
import model.LeadContactRes;
import model.LeadRes;
import model.LeadsSummaryRes;
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
					if (rootLeadEntity.isSelfApproved()) {
						leadEntity.setStatus(LeadManagementConstants.LEAD_STATUS_APPROVED);
					} else {
						leadEntity.setStatus(LeadManagementConstants.LEAD_STATUS_DRAFT);
					}
					leadEntity.setBudget(rootLeadRes.getLeadsSummaryRes().getBudget());
					leadEntity.setCurrency(rootLeadRes.getLeadsSummaryRes().getCurrency());

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
		LeadRes leadRes = prepareLeadRes(leadEntity);
		return leadRes;
	}

	@Override
	public Long updateLead(LeadRes leadRes) {
		LeadEntity originalLeadEntity = leadDAO.getLead(leadRes.getId());
		String originalBU = originalLeadEntity.getBusinessUnit();
		TransactionStatus ts = transactionManager.getTransaction(new DefaultTransactionDefinition());
		try {
			LeadEntity leadEntity = null;
			if (leadRes.getLeadsSummaryRes() != null) {
				for (String businessUnit : leadRes.getLeadsSummaryRes().getBusinessUnits()) {
					if (originalBU.equalsIgnoreCase(businessUnit)) {
						leadEntity = new LeadEntity();
						leadEntity.setSalesRep(leadRes.getLeadsSummaryRes().getSalesRep());
						leadEntity.setUpdatorId(leadRes.getCreatorId());
						leadEntity.setUpdateDate(leadRes.getCreationDate());
						leadEntity.setStatus(LeadManagementConstants.LEAD_STATUS_DRAFT);
						leadEntity.setBudget(leadRes.getLeadsSummaryRes().getBudget());
						leadEntity.setCurrency(leadRes.getLeadsSummaryRes().getCurrency());
						leadEntity.setMessage(leadRes.getMessage());
						leadDAO.updateLead(leadEntity);
					} else {
						leadEntity = new LeadEntity();
						leadEntity.setRootLeadId(leadRes.getLeadsSummaryRes().getRootLeadId());
						leadEntity.setBusinessUnit(businessUnit);
						leadEntity.setIndustry(leadRes.getLeadsSummaryRes().getIndustry());
						leadEntity.setSalesRep(leadRes.getLeadsSummaryRes().getSalesRep());
						leadEntity.setCreatorId(leadRes.getCreatorId());
						leadEntity.setCreationDate(leadRes.getCreationDate());
						leadEntity.setUpdatorId(leadRes.getCreatorId());
						leadEntity.setUpdateDate(leadRes.getCreationDate());
						leadEntity.setStatus(LeadManagementConstants.LEAD_STATUS_DRAFT);
						leadEntity.setBudget(leadRes.getLeadsSummaryRes().getBudget());
						leadEntity.setCurrency(leadRes.getLeadsSummaryRes().getCurrency());
						leadEntity.setMessage(leadRes.getMessage());
						leadDAO.insertLead(leadEntity);
					}
				}
			}
			transactionManager.commit(ts);
		} catch (Exception e) {
			transactionManager.rollback(ts);
			throw new RuntimeException(e);
		}
		return leadRes.getId();
	}

	@Override
	public List<LeadRes> getLeads() {
		List<LeadRes> leads = new ArrayList<LeadRes>();
		List<LeadEntity> leadEntityList = leadDAO.getLeads();
		for (LeadEntity leadEntity : leadEntityList) {
			LeadRes leadRes = prepareLeadRes(leadEntity);
			leads.add(leadRes);
		}
		return leads;
	}

	private LeadRes prepareLeadRes(LeadEntity leadEntity) {
		RootLeadEntity  rootLeadEntity = rootLeadDAO.getRootLead(leadEntity.getRootLeadId());

		LeadContactEntity leadContactEntity = leadContactDAO.getLeadContact(rootLeadEntity.getContactId());
		LeadContactRes leadContactRes = new LeadContactRes();
		ModelEntityMappers.mapLeadContactEntityToLeadContactRes(leadContactEntity, leadContactRes);

		LeadRes leadRes = new LeadRes();
		ModelEntityMappers.mapLeadEntityToLeadRes(leadEntity, leadRes);
		leadRes.setLeadContact(leadContactRes);

		// Map Lead Summary
		LeadsSummaryRes leadsSummaryRes = new LeadsSummaryRes();
		leadRes.setLeadsSummaryRes(ModelEntityMappers.mapLeadEntityToLeadSummary(leadEntity, leadsSummaryRes));

		// set tenure
		Date creationDate = leadEntity.getUpdateDate();
		Date today = Calendar.getInstance().getTime();
		long diff = today.getTime() - creationDate.getTime();
		leadRes.setInactiveDuration(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
		
		//Set values from root lead
		leadRes.setTenure(rootLeadEntity.getTenure());
		leadRes.setDescription(rootLeadEntity.getDescription());
		
		return leadRes;
	}

	@Override
	public List<LeadRes> searchLeads(String name, String description) {
		List<LeadRes> leads = new ArrayList<LeadRes>();
		List<LeadEntity> leadEntityList = leadDAO.searchLeads(name, description);
		for (LeadEntity leadEntity : leadEntityList) {
			LeadRes leadRes = prepareLeadRes(leadEntity);
			leads.add(leadRes);
		}
		return leads;
	}

}
