package service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.multipart.MultipartFile;

import consts.LeadManagementConstants;
import model.CreateRootLeadRes;
import model.DownloadFileRes;
import model.FilterLeadRes;
import model.LeadContactRes;
import model.LeadRes;
import model.LeadStatistictsRes;
import model.LeadsSummaryRes;
import model.Pagination;
import model.RootLeadRes;
import model.UploadFileRes;
import model.User;
import model.UserRes;
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
	private IUserService userService;

	@Autowired
	private PlatformTransactionManager transactionManager;

	@Autowired
	IFileStorageService fileStorageService;

	@Override
	public CreateRootLeadRes createRootLead(RootLeadRes rootLeadRes) {
		Long rootLeadId;
		CreateRootLeadRes createRootLeadRes = new CreateRootLeadRes();
		List<Long> craetedLeadsLst = new ArrayList<Long>();
		TransactionStatus ts = transactionManager.getTransaction(new DefaultTransactionDefinition());
		UserEntity tempSalesRep = null;
		try {
			LeadContactEntity leadContactEntity = new LeadContactEntity();
			RootLeadEntity rootLeadEntity = new RootLeadEntity();			

			ModelEntityMappers.mapRootLeadResToRootLeadEntity(rootLeadRes, rootLeadEntity);
			
			//Set Contact Id
			if (rootLeadRes.getLeadContact() != null) {
				ModelEntityMappers.mapLeadContactResToLeadContactEntity(rootLeadRes.getLeadContact(),
						leadContactEntity);
				Long leadContactId = leadContactDAO.insertLeadContact(leadContactEntity);
				rootLeadEntity.setContactId(leadContactId);
			}

			rootLeadId = rootLeadDAO.insertRootLead(rootLeadEntity);
			
			if (rootLeadRes.getLeadsSummaryRes() != null) {
				for (String businessUnit : rootLeadRes.getLeadsSummaryRes().getBusinessUnits()) {
					LeadEntity leadEntity = new LeadEntity();
					leadEntity.setRootLeadId(rootLeadId);
					leadEntity.setBusinessUnit(businessUnit);
					leadEntity.setIndustry(rootLeadRes.getLeadsSummaryRes().getIndustry());
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
						tempSalesRep = userDAO.getUserByUserId(rootLeadRes.getLeadsSummaryRes().getSalesRepId());
						if (tempSalesRep != null) {
							leadEntity.setSalesRep(tempSalesRep.getUserName());
						}
					}

					craetedLeadsLst.add(leadDAO.insertLead(leadEntity));
				}
			}
			transactionManager.commit(ts);
		} catch (Exception e) {
			transactionManager.rollback(ts);
			throw new RuntimeException(e);
		}

		createRootLeadRes.setLeads(craetedLeadsLst);
		createRootLeadRes.setRootLeadId(rootLeadId);

		return createRootLeadRes;
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
						leadEntity.setUpdatorId(leadRes.getUpdatorId());
						leadEntity.setUpdateDate(leadRes.getUpdateDate());
						String status = leadRes.getStatus() != null ? leadRes.getStatus()
								: LeadManagementConstants.LEAD_STATUS_DRAFT;
						leadEntity.setStatus(status);
						leadEntity.setBudget(leadRes.getLeadsSummaryRes().getBudget());
						leadEntity.setCurrency(leadRes.getLeadsSummaryRes().getCurrency());
						leadEntity.setMessage(leadRes.getMessage());
						leadEntity.setId(leadRes.getId());
						if (leadRes.getLeadsSummaryRes().getSalesRepId() != null) {
							leadEntity.setSalesRepId(leadRes.getLeadsSummaryRes().getSalesRepId());
						}
						leadDAO.updateLead(leadEntity);
					} else {
						leadEntity = new LeadEntity();
						leadEntity.setRootLeadId(leadRes.getLeadsSummaryRes().getRootLeadId());
						leadEntity.setBusinessUnit(businessUnit);
						leadEntity.setIndustry(leadRes.getLeadsSummaryRes().getIndustry());
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

						if (leadRes.getLeadsSummaryRes().getSalesRepId() != null) {
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
					leadEntity.setUpdateDate(leadRes.getUpdateDate());
					String status = leadRes.getStatus() != null ? leadRes.getStatus()
							: LeadManagementConstants.LEAD_STATUS_DRAFT;
					leadEntity.setStatus(status);
					leadEntity.setId(leadRes.getId());
					if (leadRes.getLeadsSummaryRes().getSalesRepId() != null) {
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
	public List<LeadRes> getLeads(String leadType, Long userId, Pagination pagination) {
		List<LeadRes> leads = new ArrayList<LeadRes>();
		List<LeadEntity> leadEntityList = leadDAO.getLeads(leadType, userId, pagination);
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
		Map<Long, UserRes> userIdUserMap;
		if (rootLeadEntity.getContactId() != null) {
			LeadContactEntity leadContactEntity = leadContactDAO.getLeadContact(rootLeadEntity.getContactId());
			LeadContactRes leadContactRes = new LeadContactRes();
			ModelEntityMappers.mapLeadContactEntityToLeadContactRes(leadContactEntity, leadContactRes);
			leadRes.setLeadContact(leadContactRes);
		}

		if (leadEntity.getCreatorId() != null) {
			userIdUserMap = userService.getCachedUsersSummaryMap();
			if (userIdUserMap != null && userIdUserMap.get(leadEntity.getCreatorId()) != null) {
				leadRes.setCreator(userIdUserMap.get(leadEntity.getCreatorId()));
			}
		}

		// Map Lead Summary
		LeadsSummaryRes leadsSummaryRes = new LeadsSummaryRes();
		ModelEntityMappers.mapLeadEntityToLeadSummary(leadEntity, leadsSummaryRes);
		if (leadEntity.getSalesRepId() != null) {
			// if (userService.getCachedUsersSummaryMap() != null
			// && userService.getCachedUsersSummaryMap().get(leadEntity.getSalesRepId()) !=
			// null) {
			userIdUserMap = userService.getCachedUsersSummaryMap();
			if (userIdUserMap != null && userIdUserMap.get(leadEntity.getSalesRepId()) != null) {
				leadsSummaryRes.setSalesRep(userIdUserMap.get(leadEntity.getSalesRepId()));
			}
			// }
		}
		leadRes.setLeadsSummaryRes(leadsSummaryRes);

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
		leadRes.setSourceInfo(rootLeadEntity.getSourceInfo());
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
	public List<LeadRes> filterLeads(FilterLeadRes filterLeadRes, Pagination pagination) {
		List<LeadRes> leads = new ArrayList<LeadRes>();
		List<LeadEntity> leadEntityList = leadDAO.filterLeads(filterLeadRes, pagination);
		for (LeadEntity leadEntity : leadEntityList) {
			LeadRes leadRes = prepareLeadRes(leadEntity);
			leads.add(leadRes);
		}
		return leads;
	}

	@Override
	public LeadStatistictsRes getLeadStatistics(FilterLeadRes filterLeadRes, Boolean busummary, Long userId) {
		return leadDAO.getLeadStatistics(filterLeadRes, busummary, userId);
	}

	@Override
	public ByteArrayInputStream getLeadStatisticsReport(FilterLeadRes filterLeadRes, Boolean busummary, Long userId)
			throws IOException {
		String[] COLUMNs = { "Status", "Count" };

		LeadStatistictsRes LeadStatistictsRes = leadDAO.getLeadStatistics(filterLeadRes, busummary, userId);
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {

			Sheet sheet = workbook.createSheet("Leads");

			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setColor(IndexedColors.BLUE.getIndex());

			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFont(headerFont);

			// Row for Header
			Row headerRow = sheet.createRow(0);

			// Header
			for (int col = 0; col < COLUMNs.length; col++) {
				Cell cell = headerRow.createCell(col);
				cell.setCellValue(COLUMNs[col]);
				cell.setCellStyle(headerCellStyle);
			}

			Map<String, Long> leadStatusCountMap = LeadStatistictsRes.getLeadStatusCountMap();
			int rowIdx = 1;
			for (Map.Entry<String, Long> entry : leadStatusCountMap.entrySet()) {
				String status = entry.getKey();
				Long statusCount = (Long) entry.getValue();
				Row row = sheet.createRow(rowIdx++);
				row.createCell(0).setCellValue(status);
				row.createCell(1).setCellValue(statusCount);
			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		}
	}

	@Override
	public Long updateLeadAttachment(Long leadId, String path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UploadFileRes uploadLeadAttachment(Long id, Long uerId, List<MultipartFile> files) throws IOException {
		UploadFileRes uploadFileRes = fileStorageService.uploadLeadAttachment(id, files);
		LeadEntity leadEntity = new LeadEntity();
		leadEntity.setUpdateDate(new java.sql.Date((new Date()).getTime()));
		leadEntity.setUpdatorId(uerId);
		leadEntity.setId(id);
		leadEntity.setAttachment(uploadFileRes.getFileName());
		leadDAO.updateLeadAttachment(leadEntity);
		return uploadFileRes;
	}

	@Override
	public UploadFileRes uploadLeadAttachments(List<Long> leadIds, Long userId, List<MultipartFile> files)
			throws IOException {
		UploadFileRes uploadFileRes = null;
		for (Long id : leadIds) {
			uploadFileRes = this.uploadLeadAttachment(id, userId, files);
		}
		return uploadFileRes;
	}

	@Override
	public DownloadFileRes downloadLeadAttachment(Long leadId, String name) throws IOException {
		return fileStorageService.downloadLeadAttachment(leadId, name);
	}

}
