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
import model.FilterLeadRes;
import model.LeadContactRes;
import model.LeadRes;
import model.LeadStatistictsRes;
import model.LeadsSummaryRes;
import model.RootLeadRes;
import repository.ILeadContactDAO;
import repository.ILeadDAO;
import repository.IRootLeadDAO;
import repository.IUserDAO;
import repository.entity.LeadContactEntity;
import repository.entity.LeadEntity;
import repository.entity.RootLeadEntity;
import repository.entity.UserEntity;
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
	private IUserDAO userDAO;

	@Autowired
	private PlatformTransactionManager transactionManager;

	@Override
	public Long createRootLead(RootLeadRes rootLeadRes) {
		Long rootLeadId;
		TransactionStatus ts = transactionManager.getTransaction(new DefaultTransactionDefinition());
		try {
			LeadContactEntity leadContactEntity = new LeadContactEntity();
			RootLeadEntity rootLeadEntity = new RootLeadEntity();

			if (rootLeadRes.getLeadContact() != null) {
				ModelEntityMappers.mapLeadContactResToLeadContactEntity(rootLeadRes.getLeadContact(),
						leadContactEntity);
				Long leadContactId = leadContactDAO.insertLeadContact(leadContactEntity);
				rootLeadEntity.setContactId(leadContactId);
			}

			ModelEntityMappers.mapRootLeadResToRootLeadEntity(rootLeadRes, rootLeadEntity);

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
					if (rootLeadRes.isSelfApproved()) {
						leadEntity.setStatus(LeadManagementConstants.LEAD_STATUS_APPROVED);
					} else {
						leadEntity.setStatus(LeadManagementConstants.LEAD_STATUS_DRAFT);
					}
					leadEntity.setBudget(rootLeadRes.getLeadsSummaryRes().getBudget());
					leadEntity.setCurrency(rootLeadRes.getLeadsSummaryRes().getCurrency());

					UserEntity user = userDAO.getUserByUserId(rootLeadRes.getCreatorId());
					leadEntity.setOriginatingBusinessUnit(user.getBusinessUnit());

					if (rootLeadRes.getLeadsSummaryRes().getSalesRepId() != null) {
						leadEntity.setSalesRepId(rootLeadRes.getLeadsSummaryRes().getSalesRepId());
					}

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
			if (leadRes.getLeadsSummaryRes() != null && leadRes.getLeadsSummaryRes().getBusinessUnits() != null) {
				for (String businessUnit : leadRes.getLeadsSummaryRes().getBusinessUnits()) {
					if (originalBU.equalsIgnoreCase(businessUnit)) {
						leadEntity = new LeadEntity();
						leadEntity.setSalesRep(leadRes.getLeadsSummaryRes().getSalesRep());
						leadEntity.setUpdatorId(leadRes.getUpdatorId());
						leadEntity.setUpdateDate(leadRes.getUpdateDate());
						String status = leadRes.getStatus() != null ? leadRes.getStatus()
								: LeadManagementConstants.LEAD_STATUS_DRAFT;
						leadEntity.setStatus(status);
						leadEntity.setBudget(leadRes.getLeadsSummaryRes().getBudget());
						leadEntity.setCurrency(leadRes.getLeadsSummaryRes().getCurrency());
						leadEntity.setMessage(leadRes.getMessage());
						leadEntity.setId(leadRes.getId());
						if(leadRes.getLeadsSummaryRes().getSalesRepId() != null) {
							leadEntity.setSalesRepId(leadRes.getLeadsSummaryRes().getSalesRepId());
						}
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

						UserEntity user = userDAO.getUserByUserId(leadRes.getCreatorId());
						leadEntity.setOriginatingBusinessUnit(user.getBusinessUnit());
						
						if(leadRes.getLeadsSummaryRes().getSalesRepId() != null) {
							leadEntity.setSalesRepId(leadRes.getLeadsSummaryRes().getSalesRepId());
						}

						leadDAO.insertLead(leadEntity);
					}
				}

				if (!leadRes.getLeadsSummaryRes().getBusinessUnits().contains(originalBU)) {
					leadDAO.deleteLead(leadRes.getId());
				}

			} else {
				leadEntity = new LeadEntity();
				leadEntity.setUpdatorId(leadRes.getUpdatorId());
				Date updateDate = leadRes.getUpdateDate() != null ? leadRes.getUpdateDate()
						: Calendar.getInstance().getTime();
				leadEntity.setUpdateDate(new java.sql.Date(updateDate.getTime()));
				leadEntity.setStatus(LeadManagementConstants.LEAD_STATUS_DRAFT);
				String message = leadRes.getMessage() != null ? leadRes.getMessage() : originalLeadEntity.getMessage();
				leadEntity.setMessage(message);
				if (leadRes.getLeadsSummaryRes() != null) {
					Double budget = leadRes.getLeadsSummaryRes().getBudget() != null
							? leadRes.getLeadsSummaryRes().getBudget()
							: originalLeadEntity.getBudget();
					leadEntity.setBudget(budget);
					String currency = leadRes.getLeadsSummaryRes().getCurrency() != null
							? leadRes.getLeadsSummaryRes().getCurrency()
							: originalLeadEntity.getCurrency();
					leadEntity.setCurrency(currency);
					String salesRep = leadRes.getLeadsSummaryRes().getSalesRep() != null
							? leadRes.getLeadsSummaryRes().getSalesRep()
							: originalLeadEntity.getSalesRep();
					leadEntity.setSalesRep(salesRep);
					leadEntity.setUpdateDate(leadRes.getUpdateDate());
					String status = leadRes.getStatus() != null ? leadRes.getStatus()
							: LeadManagementConstants.LEAD_STATUS_DRAFT;
					leadEntity.setStatus(status);
					leadEntity.setId(leadRes.getId());
					if(leadRes.getLeadsSummaryRes().getSalesRepId() != null) {
						leadEntity.setSalesRepId(leadRes.getLeadsSummaryRes().getSalesRepId());
					}
				}
				leadDAO.updateLead(leadEntity);
			}
			transactionManager.commit(ts);
		} catch (Exception e) {
			transactionManager.rollback(ts);
			throw new RuntimeException(e);
		}
		return leadRes.getId();
	}

	LeadEntity cloneLead(LeadEntity leadEntity) {
		LeadEntity clonedLeadEntity = new LeadEntity();
		clonedLeadEntity.setBudget(leadEntity.getBudget());
		clonedLeadEntity.setCurrency(leadEntity.getCurrency());
		clonedLeadEntity.setBusinessUnit(leadEntity.getBusinessUnit());

		// clonedLeadEntity.setCreationDate(creationDate);
		// clonedLeadEntity.setCreatorId(creatorId);
		// clonedLeadEntity.setUpdateDate(updateDate);
		// clonedLeadEntity.setUpdatorId(updatorId);

		// clonedLeadEntity.setDeleted(deleted);
		// clonedLeadEntity.setId(id);

		clonedLeadEntity.setIndustry(leadEntity.getIndustry());
		clonedLeadEntity.setMessage(leadEntity.getMessage());
		clonedLeadEntity.setRootLeadId(leadEntity.getRootLeadId());
		clonedLeadEntity.setSalesRep(leadEntity.getSalesRep());
		clonedLeadEntity.setStatus(LeadManagementConstants.LEAD_STATUS_DRAFT);
		return clonedLeadEntity;
	}

	@Override
	public List<LeadRes> getLeads(String leadType,Long userId) {
		List<LeadRes> leads = new ArrayList<LeadRes>();
		List<LeadEntity> leadEntityList = leadDAO.getLeads(leadType,userId);
		for (LeadEntity leadEntity : leadEntityList) {
			LeadRes leadRes = prepareLeadRes(leadEntity);
			leads.add(leadRes);
		}
		return leads;
	}

	private LeadRes prepareLeadRes(LeadEntity leadEntity) {
		RootLeadEntity rootLeadEntity = rootLeadDAO.getRootLead(leadEntity.getRootLeadId());
		LeadRes leadRes = new LeadRes();
		ModelEntityMappers.mapLeadEntityToLeadRes(leadEntity, leadRes);

		if (rootLeadEntity.getContactId() != null) {
			LeadContactEntity leadContactEntity = leadContactDAO.getLeadContact(rootLeadEntity.getContactId());
			LeadContactRes leadContactRes = new LeadContactRes();
			ModelEntityMappers.mapLeadContactEntityToLeadContactRes(leadContactEntity, leadContactRes);
			leadRes.setLeadContact(leadContactRes);
		}

		// Map Lead Summary
		LeadsSummaryRes leadsSummaryRes = new LeadsSummaryRes();
		leadRes.setLeadsSummaryRes(ModelEntityMappers.mapLeadEntityToLeadSummary(leadEntity, leadsSummaryRes));

		// set tenure
		if (leadEntity.getUpdateDate() != null) {
			Date creationDate = leadEntity.getUpdateDate();
			Date today = Calendar.getInstance().getTime();
			long diff = today.getTime() - creationDate.getTime();
			leadRes.setInactiveDuration(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
		}

		// Set values from root lead
		leadRes.setTenure(rootLeadEntity.getTenure());
		leadRes.setDescription(rootLeadEntity.getDescription());
		leadRes.setCustName(rootLeadEntity.getCustName());
		leadRes.setSource(rootLeadEntity.getSource());

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

	@Override
	public List<LeadRes> filterLeads(FilterLeadRes filterLeadRes) {
		List<LeadRes> leads = new ArrayList<LeadRes>();
		List<LeadEntity> leadEntityList = leadDAO.filterLeads(filterLeadRes);
		for (LeadEntity leadEntity : leadEntityList) {
			LeadRes leadRes = prepareLeadRes(leadEntity);
			leads.add(leadRes);
		}
		return leads;
	}

	@Override
	public LeadStatistictsRes getLeadStatistics(FilterLeadRes filterLeadRes,Boolean busummary,Long userId) {	
		return leadDAO.getLeadStatistics(filterLeadRes,busummary,userId);
	}

}
