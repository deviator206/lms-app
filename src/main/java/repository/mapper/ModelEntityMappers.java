package repository.mapper;

import java.util.ArrayList;
import java.util.List;

import model.LeadContactRes;
import model.LeadRes;
import model.LeadsSummaryRes;
import model.RootLeadRes;
import repository.entity.LeadContactEntity;
import repository.entity.LeadEntity;
import repository.entity.RootLeadEntity;

public class ModelEntityMappers {

	public static LeadContactEntity mapLeadContactResToLeadContactEntity(LeadContactRes leadContactRes,
			LeadContactEntity leadContactEntity) {
		leadContactEntity.setId(leadContactRes.getId());
		leadContactEntity.setName(leadContactRes.getName());
		leadContactEntity.setEmail(leadContactRes.getEmail());
		leadContactEntity.setPhoneNumber(leadContactRes.getPhoneNumber());
		leadContactEntity.setCountry(leadContactRes.getCountry());
		leadContactEntity.setState(leadContactRes.getState());
		leadContactEntity.setDesignation(leadContactRes.getDesignation());
		return leadContactEntity;
	}

	public static RootLeadEntity mapRootLeadResToRootLeadEntity(RootLeadRes rootLeadRes,
			RootLeadEntity rootLeadEntity) {
		rootLeadEntity.setId(rootLeadRes.getId());
		rootLeadEntity.setSource(rootLeadRes.getSource());
		rootLeadEntity.setCustName(rootLeadRes.getCustName());
		rootLeadEntity.setDescription(rootLeadRes.getDescription());
		rootLeadEntity.setContactId(rootLeadRes.getLeadContact().getId());
		rootLeadEntity.setDeleted(rootLeadRes.isDeleted());
		rootLeadEntity.setCreationDate(rootLeadRes.getCreationDate());
		rootLeadEntity.setCreatorId(rootLeadRes.getCreatorId());
		rootLeadEntity.setSelfApproved(rootLeadRes.isSelfApproved());
		rootLeadEntity.setTenure(rootLeadRes.getTenure());
		if(rootLeadRes.getLeadsSummaryRes() != null) {
			rootLeadEntity.setSalesRep(rootLeadRes.getLeadsSummaryRes().getSalesRep());
		}		
		return rootLeadEntity;
	}

	public static RootLeadRes mapRootLeadEntityToRootLeadRes(RootLeadEntity rootLeadEntity, RootLeadRes rootLeadRes) {
		rootLeadRes.setId(rootLeadEntity.getId());
		rootLeadRes.setSource(rootLeadEntity.getSource());
		rootLeadRes.setCustName(rootLeadEntity.getCustName());
		rootLeadRes.setDescription(rootLeadEntity.getDescription());
		LeadContactRes leadContactRes = new LeadContactRes();
		leadContactRes.setId(rootLeadEntity.getContactId());
		rootLeadRes.setDeleted(rootLeadEntity.isDeleted());
		rootLeadRes.setCreationDate(rootLeadEntity.getCreationDate());
		rootLeadRes.setCreatorId(rootLeadEntity.getCreatorId());
		return rootLeadRes;
	}

	public static LeadContactRes mapLeadContactEntityToLeadContactRes(LeadContactEntity leadContactEntity,
			LeadContactRes leadContactRes) {
		leadContactRes.setId(leadContactEntity.getId());
		leadContactRes.setName(leadContactEntity.getName());
		leadContactRes.setEmail(leadContactEntity.getEmail());
		leadContactRes.setPhoneNumber(leadContactEntity.getPhoneNumber());
		leadContactRes.setCountry(leadContactEntity.getCountry());
		leadContactRes.setState(leadContactEntity.getState());
		leadContactRes.setDesignation(leadContactEntity.getDesignation());
		return leadContactRes;
	}

	/*
	 * public static LeadEntity mapLeadResToLeadEntity(LeadRes leadRes, LeadEntity
	 * leadEntity) { leadEntity.setId(leadRes.getId());
	 * leadEntity.setBusinessUnit(leadRes.getBusinessUnit());
	 * leadEntity.setStatus(leadRes.getStatus());
	 * leadEntity.setRootLeadId(leadRes.getRootLead().getId());
	 * leadEntity.setDeleted(leadRes.isDeleted());
	 * leadEntity.setCreationDate(leadRes.getCreationDate());
	 * leadEntity.setUpdateDate(leadRes.getUpdateDate());
	 * leadEntity.setCreatorId(leadRes.getCreatorId());
	 * leadEntity.setUpdatorId(leadRes.getUpdatorId()); return leadEntity; }
	 */

	public static LeadRes mapLeadEntityToLeadRes(LeadEntity leadEntity, LeadRes leadRes) {
		leadRes.setId(leadEntity.getId());
		leadRes.setDeleted(leadEntity.isDeleted());
		leadRes.setCreationDate(leadEntity.getCreationDate());
		leadRes.setUpdateDate(leadEntity.getUpdateDate());
		leadRes.setCreatorId(leadEntity.getCreatorId());
		leadRes.setUpdatorId(leadEntity.getUpdatorId());
		leadRes.setStatus(leadEntity.getStatus());
		return leadRes;
	}

	public static LeadsSummaryRes mapLeadEntityToLeadSummary(LeadEntity leadEntity, LeadsSummaryRes leadsSummaryRes) {
		leadsSummaryRes.setRootLeadId(leadEntity.getRootLeadId());
		leadsSummaryRes.setBudget(leadEntity.getBudget());
		leadsSummaryRes.setCurrency(leadEntity.getCurrency());
		List<String> businessUnits = new ArrayList<String>(1);
		businessUnits.add(leadEntity.getBusinessUnit());
		leadsSummaryRes.setBusinessUnits(businessUnits);
		leadsSummaryRes.setIndustry(leadEntity.getIndustry());
		leadsSummaryRes.setSalesRep(leadEntity.getSalesRep());
		return leadsSummaryRes;
	}

	public static LeadEntity mapLeadResToLeadEntity(LeadRes leadRes, LeadEntity leadEntity) {
		leadEntity.setId(leadRes.getId());
		
		if(leadRes.getLeadsSummaryRes() != null) {
			leadEntity.setRootLeadId(leadRes.getLeadsSummaryRes().getRootLeadId());
			leadEntity.setBusinessUnit(leadRes.getLeadsSummaryRes().getBusinessUnits().get(0));
			leadEntity.setIndustry(leadRes.getLeadsSummaryRes().getIndustry());
			leadEntity.setSalesRep(leadRes.getLeadsSummaryRes().getSalesRep());
			leadEntity.setBudget(leadRes.getLeadsSummaryRes().getBudget());
			leadEntity.setCurrency(leadRes.getLeadsSummaryRes().getCurrency());
		}
		
		leadEntity.setDeleted(leadRes.isDeleted());
		leadEntity.setCreationDate(leadRes.getCreationDate());
		leadEntity.setUpdateDate(leadRes.getUpdateDate());
		leadEntity.setCreatorId(leadRes.getCreatorId());
		leadEntity.setUpdatorId(leadRes.getUpdatorId());
		return leadEntity;
	}

}
