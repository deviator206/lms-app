package repository.mapper;

import java.util.ArrayList;
import java.util.List;

import model.LeadContactRes;
import model.LeadRes;
import model.LeadsSummaryRes;
import model.MarketIntelligenceInfoReq;
import model.MarketIntelligenceInfoRes;
import model.MarketIntelligenceReq;
import model.MarketIntelligenceRes;
import model.RootLeadRes;
import model.UserRes;
import repository.entity.LeadContactEntity;
import repository.entity.LeadEntity;
import repository.entity.MarketIntelligenceEntity;
import repository.entity.MarketIntelligenceInfoEntity;
import repository.entity.RootLeadEntity;
import repository.entity.UserEntity;

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

		if (rootLeadRes.getLeadContact() != null) {
			rootLeadEntity.setContactId(rootLeadRes.getLeadContact().getId());
		}

		rootLeadEntity.setDeleted(rootLeadRes.isDeleted());
		rootLeadEntity.setCreationDate(rootLeadRes.getCreationDate());
		rootLeadEntity.setCreatorId(rootLeadRes.getCreatorId());
		rootLeadEntity.setSelfApproved(rootLeadRes.isSelfApproved());
		rootLeadEntity.setTenure(rootLeadRes.getTenure());
		if (rootLeadRes.getLeadsSummaryRes() != null) {
			rootLeadEntity.setSalesRepId(rootLeadRes.getLeadsSummaryRes().getSalesRepId());
		}
		rootLeadEntity.setSourceInfo(rootLeadRes.getSourceInfo());
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

	public static MarketIntelligenceRes mapMiEntityToMiRes(MarketIntelligenceEntity miEntity,
			MarketIntelligenceRes miRes) {
		miRes.setId(miEntity.getId());
		miRes.setType(miEntity.getType());
		miRes.setName(miEntity.getName());
		miRes.setStatus(miEntity.getStatus());
		miRes.setDescription(miEntity.getDescription());
		miRes.setInvestment(miEntity.getInvestment());
		miRes.setRootLeadId(miEntity.getLeadId());
		miRes.setCreationDate(miEntity.getCreationDate());
		miRes.setCreationDate(miEntity.getCreationDate());
		miRes.setUpdatorId(miEntity.getUpdatorId());
		miRes.setUpdateDate(miEntity.getUpdateDate());
		miRes.setAttachment(miEntity.getAttachment());
		return miRes;
	}

	/*
	 * public static MarketIntelligenceEntity
	 * mapMiResToMiEntity(MarketIntelligenceRes miRes, MarketIntelligenceEntity
	 * miEntity) { // miEntity.setId(miRes.getId());
	 * miEntity.setType(miRes.getType()); miEntity.setName(miRes.getName());
	 * miEntity.setStatus(miRes.getStatus());
	 * miEntity.setDescription(miRes.getDescription());
	 * miEntity.setInvestment(miRes.getInvestment());
	 * miEntity.setLeadId(miRes.getRootLeadId());
	 * miEntity.setCreatorId(miRes.getCreatorId());
	 * miEntity.setCreationDate(miRes.getCreationDate());
	 * miEntity.setUpdatorId(miRes.getUpdatorId());
	 * miEntity.setUpdateDate(miRes.getUpdateDate()); return miEntity; }
	 */

	public static MarketIntelligenceEntity mapMiReqToMiEntity(MarketIntelligenceReq miRes,
			MarketIntelligenceEntity miEntity) {
		// miEntity.setId(miRes.getId());
		miEntity.setType(miRes.getType());
		miEntity.setName(miRes.getName());
		miEntity.setStatus(miRes.getStatus());
		miEntity.setDescription(miRes.getDescription());
		miEntity.setInvestment(miRes.getInvestment());
		miEntity.setLeadId(miRes.getRootLeadId());
		miEntity.setCreatorId(miRes.getCreatorId());
		miEntity.setCreationDate(miRes.getCreationDate());
		miEntity.setUpdatorId(miRes.getUpdatorId());
		miEntity.setUpdateDate(miRes.getUpdateDate());
		return miEntity;
	}

	public static MarketIntelligenceInfoEntity mapMiInfoResToMiInfoEntity(MarketIntelligenceInfoRes miRes,
			MarketIntelligenceInfoEntity miEntity) {
		// miEntity.setId(miRes.getId());
		miEntity.setMiId(miRes.getMiId());
		miEntity.setInfo(miRes.getInfo());
		miEntity.setCreationDate(miRes.getCreationDate());
		miEntity.setCreatorId(miRes.getCreatorId());
		return miEntity;
	}
	
	public static MarketIntelligenceInfoEntity mapMiInfoReqToMiInfoEntity(MarketIntelligenceInfoReq miRes,
			MarketIntelligenceInfoEntity miEntity) {
		miEntity.setMiId(miRes.getMiId());
		miEntity.setInfo(miRes.getInfo());
		miEntity.setCreationDate(miRes.getCreationDate());
		miEntity.setCreatorId(miRes.getCreatorId());
		return miEntity;
	}

	public static MarketIntelligenceInfoRes mapMiInfoEntityToMiInfoRes(MarketIntelligenceInfoEntity miEntity,
			MarketIntelligenceInfoRes miRes) {
		miRes.setId(miEntity.getId());
		miRes.setMiId(miEntity.getMiId());
		miRes.setInfo(miEntity.getInfo());
		miRes.setCreationDate(miEntity.getCreationDate());
		miRes.setCreatorId(miEntity.getCreatorId());
		return miRes;
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
		leadRes.setAttachment(leadEntity.getAttachment());
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
		leadsSummaryRes.setSalesRepId(leadEntity.getSalesRepId());
		return leadsSummaryRes;
	}

	public static LeadEntity mapLeadResToLeadEntity(LeadRes leadRes, LeadEntity leadEntity) {
		leadEntity.setId(leadRes.getId());

		if (leadRes.getLeadsSummaryRes() != null) {
			leadEntity.setRootLeadId(leadRes.getLeadsSummaryRes().getRootLeadId());
			leadEntity.setBusinessUnit(leadRes.getLeadsSummaryRes().getBusinessUnits().get(0));
			leadEntity.setIndustry(leadRes.getLeadsSummaryRes().getIndustry());
			leadEntity.setSalesRepId(leadRes.getLeadsSummaryRes().getSalesRepId());
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

	public static UserRes mapUserEntityToUserRes(UserEntity userEntity, UserRes user) {
		user.setUserId(userEntity.getId());
		user.setUserName(userEntity.getUserName());
		user.setEmail(userEntity.getEmail());
		user.setEnabled(userEntity.isEnabled());
		user.setUserDisplayName(userEntity.getUserDisplayName());
		user.setBusinessUnit(userEntity.getBusinessUnit());
		return user;
	}

}
