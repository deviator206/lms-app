package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import consts.LeadManagementConstants;
import model.FilterLeadRes;
import model.LeadStatistictsRes;
import model.Pagination;
import repository.entity.LeadEntity;
import repository.entity.UserEntity;
import repository.mapper.LeadRowMapper;
import security.SqlSafeUtil;

@Repository
public class LeadDAOImpl implements ILeadDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private IUserDAO userDAO;

	@Override
	public LeadEntity getLead(Long id) {
		String sql = "SELECT ID, BU,SALES_REP,STATUS,ROOT_ID,DELETED,CREATION_DATE,CREATOR_ID,UPDATE_DATE,UPDATOR_ID,BUDGET,CURRENCY,MESSAGE, SALES_REP_ID,ATTACHMENT FROM LEADS WHERE ID = ?";
		RowMapper<LeadEntity> rowMapper = new LeadRowMapper();
		return this.jdbcTemplate.queryForObject(sql, rowMapper, new Object[] { id });
	}

	@Override
	public Long insertLead(LeadEntity leadEntity) {
		Long id = null;
		String sql = "INSERT INTO LEADS (BU,STATUS,ROOT_ID,DELETED,CREATION_DATE,CREATOR_ID,UPDATE_DATE,UPDATOR_ID,SALES_REP,BUDGET,CURRENCY,MESSAGE,ORIGINATING_BU,SALES_REP_ID, INDUSTRY) \r\n"
				+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, leadEntity.getBusinessUnit());
				ps.setString(2, leadEntity.getStatus());
				ps.setLong(3, leadEntity.getRootLeadId());
				ps.setBoolean(4, leadEntity.isDeleted());
				ps.setDate(5, leadEntity.getCreationDate());
				ps.setLong(6, leadEntity.getCreatorId());
				ps.setDate(7, leadEntity.getUpdateDate());
				ps.setLong(8, leadEntity.getUpdatorId());
				ps.setString(9, leadEntity.getSalesRep());

				if (leadEntity.getBudget() != null) {
					ps.setDouble(10, leadEntity.getBudget());
				} else {
					ps.setNull(10, Types.DOUBLE);
				}

				ps.setString(11, leadEntity.getCurrency());
				ps.setString(12, leadEntity.getMessage());

				ps.setString(13, leadEntity.getOriginatingBusinessUnit());

				if (leadEntity.getSalesRepId() != null) {
					ps.setDouble(14, leadEntity.getSalesRepId());
				} else {
					ps.setNull(14, Types.INTEGER);
				}

				ps.setString(15, leadEntity.getIndustry());

				return ps;
			}
		}, keyHolder);
		id = keyHolder.getKey().longValue();
		return id;
	}

	@Override
	public boolean deleteLead(Long id) {
		String sql = "DELETE FROM LEADS WHERE ID = ?";
		int affectedRows = jdbcTemplate.update(sql, id);
		return affectedRows == 0 ? false : true;
	}

	@Override
	public boolean updateLead(LeadEntity leadEntity) {
		String sql = "UPDATE LEADS SET BUDGET = ?, CURRENCY = ?, SALES_REP = ?, STATUS = ?, MESSAGE = ?, UPDATE_DATE = ?,UPDATOR_ID = ? WHERE ID = ?;";
		int affectedRows = jdbcTemplate.update(sql, leadEntity.getBudget(), leadEntity.getCurrency(),
				leadEntity.getSalesRep(), leadEntity.getStatus(), leadEntity.getMessage(), leadEntity.getUpdateDate(),
				leadEntity.getUpdatorId(), leadEntity.getId());
		return affectedRows == 0 ? false : true;
	}

	@Override
	public List<LeadEntity> getLeads(String leadtype, Long userId, Pagination pagination) {
		String sql = "SELECT ID, BU,SALES_REP_ID,STATUS,ROOT_ID,DELETED,CREATION_DATE,CREATOR_ID,UPDATE_DATE,UPDATOR_ID,BUDGET,CURRENCY,MESSAGE,ATTACHMENT FROM LEADS WHERE 1 = 1";

		if (userId != null) {
			if (LeadManagementConstants.LEAD_TYPE_ASSIGNED.equalsIgnoreCase(leadtype)) {
				sql = sql + " AND SALES_REP_ID = " + userId;
			} else if (LeadManagementConstants.LEAD_TYPE_GENERATED.equalsIgnoreCase(leadtype)) {
				sql = sql + " AND CREATOR_ID = " + userId;
			} else if (LeadManagementConstants.LEAD_TYPE_BOTH.equalsIgnoreCase(leadtype)) {
				sql = sql + " AND CREATOR_ID = " + userId + " AND SALES_REP_ID = " + userId;
			}
		}

		sql = sql + " ORDER BY CREATION_DATE DESC ";

		if ((pagination != null) && pagination.isPaginatedQuery()) {
			sql = RepositoryHelper.getPaginatedQuery(sql, pagination.getStart(), pagination.getPageSize());
		}

		RowMapper<LeadEntity> rowMapper = new LeadRowMapper();
		return this.jdbcTemplate.query(sql, rowMapper);
	}

	@Override
	public List<LeadEntity> searchLeads(String name, String description) {
		RowMapper<LeadEntity> rowMapper = new LeadRowMapper();
		String sql = "SELECT LEADS.ID, LEADS.BU,LEADS.SALES_REP_ID,LEADS.STATUS,LEADS.ROOT_ID,LEADS.DELETED,LEADS.CREATION_DATE,LEADS.CREATOR_ID,LEADS.UPDATE_DATE,LEADS.UPDATOR_ID,LEADS.BUDGET,LEADS.CURRENCY,LEADS.MESSAGE,LEADS.ATTACHMENT FROM LEADS,ROOT_LEAD WHERE LEADS.ROOT_ID = ROOT_LEAD.ID AND ROOT_LEAD.DESCRIPTION LIKE ?";
		// String likePattern = "'%"+ description + "%'";
		return jdbcTemplate.query(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql);
				ps.setString(1, "%" + description + "%");
				return ps;
			}
		}, rowMapper);

	}

	@Override
	public List<LeadEntity> filterLeads(FilterLeadRes filterLeadRes, Pagination pagination) {
		RowMapper<LeadEntity> rowMapper = new LeadRowMapper();

		if (!SqlSafeUtil.isSqlInjectionSafe(filterLeadRes.getCustName())) {
			throw new RuntimeException("SQL Injection Error");
		}

		String query = "SELECT LEADS.ID, LEADS.BU,LEADS.SALES_REP_ID,LEADS.STATUS,LEADS.ROOT_ID,LEADS.DELETED,LEADS.CREATION_DATE,LEADS.CREATOR_ID,"
				+ "LEADS.UPDATE_DATE,LEADS.UPDATOR_ID,LEADS.BUDGET,LEADS.CURRENCY,LEADS.MESSAGE,LEADS.ATTACHMENT,ROOT_LEAD.SOURCE,LEADS.INDUSTRY,"
				+ "ROOT_LEAD.TENURE,LEAD_CONTACT.COUNTRY,LEAD_CONTACT.STATE " + "FROM LEADS,ROOT_LEAD,LEAD_CONTACT "
				+ "WHERE LEADS.ROOT_ID = ROOT_LEAD.ID AND ROOT_LEAD.CONTACT_ID = LEAD_CONTACT.ID";

		query = getFilterLeadsQuery(filterLeadRes, query);

		query = query + " ORDER BY CREATION_DATE DESC ";

		if ((pagination != null) && pagination.isPaginatedQuery()) {
			query = RepositoryHelper.getPaginatedQuery(query, pagination.getStart(), pagination.getPageSize());
		}

		return this.jdbcTemplate.query(query, rowMapper);
	}

	private String getFilterLeadsQuery(FilterLeadRes filterLeadRes, String query) {
		if (filterLeadRes.getCustName() != null && !filterLeadRes.getCustName().isEmpty()) {
			query = query + " AND ROOT_LEAD.CUST_NAME LIKE '%" + filterLeadRes.getCustName() + "%'";
		}

		if (filterLeadRes.getStatus() != null && !filterLeadRes.getStatus().isEmpty()) {
			query = query + " AND LEADS.STATUS = '" + filterLeadRes.getStatus() + "'";
		}

		if (filterLeadRes.getSalesRepId() != null) {
			query = query + " AND SALES_REP_ID = " + filterLeadRes.getSalesRepId();
		}

		if (filterLeadRes.getSalesRep() != null && !filterLeadRes.getSalesRep().isEmpty()) {
			query = query + " AND LEADS.SALES_REP LIKE '%" + filterLeadRes.getSalesRep() + "%'";
		}

		if (filterLeadRes.getStartDate() != null && filterLeadRes.getEndDate() != null) {
			query = query + " AND LEADS.CREATION_DATE BETWEEN '" + filterLeadRes.getStartDate() + "' AND '"
					+ filterLeadRes.getEndDate() + "'";
		} else if (filterLeadRes.getStartDate() != null) {
			query = query + " AND LEADS.CREATION_DATE >= '" + filterLeadRes.getStartDate() + "'";
		} else if (filterLeadRes.getEndDate() != null) {
			query = query + " AND LEADS.CREATION_DATE <= '" + filterLeadRes.getEndDate() + "'";
		}

		if (filterLeadRes.getFromBu() != null && !filterLeadRes.getFromBu().isEmpty()
				&& (!LeadManagementConstants.ALL_BU.equalsIgnoreCase(filterLeadRes.getFromBu()))) {
			query = query + " AND LEADS.ORIGINATING_BU = '" + filterLeadRes.getFromBu() + "'";
		}

		if (filterLeadRes.getToBu() != null && !filterLeadRes.getToBu().isEmpty()
				&& (!LeadManagementConstants.ALL_BU.equalsIgnoreCase(filterLeadRes.getToBu()))) {
			query = query + " AND LEADS.BU = '" + filterLeadRes.getToBu() + "'";
		}

		if (filterLeadRes.getCreatorId() != null) {
			query = query + " AND LEADS.BU = " + filterLeadRes.getCreatorId();
		}

		if (filterLeadRes.getSource() != null && !filterLeadRes.getSource().isEmpty()) {
			query = query + " AND ROOT_LEAD.SOURCE = '" + filterLeadRes.getSource() + "'";
		}

		if (filterLeadRes.getIndustry() != null && !filterLeadRes.getIndustry().isEmpty()) {
			query = query + " AND LEADS.INDUSTRY = '" + filterLeadRes.getIndustry() + "'";
		}

		if (filterLeadRes.getTenure() != null && !filterLeadRes.getTenure().isEmpty()) {
			query = query + " AND ROOT_LEAD.TENURE = '" + filterLeadRes.getTenure() + "'";
		}

		if (filterLeadRes.getCountry() != null && !filterLeadRes.getCountry().isEmpty()) {
			query = query + " AND LEAD_CONTACT.COUNTRY = '" + filterLeadRes.getCountry() + "'";
		}

		if (filterLeadRes.getState() != null && !filterLeadRes.getState().isEmpty()) {
			query = query + " AND LEAD_CONTACT.STATE = '" + filterLeadRes.getState() + "'";
		}

		return query;
	}

	private String getLeadStatisticsQuery(FilterLeadRes filterLeadRes, String query) {
		if (filterLeadRes.getCustName() != null && !filterLeadRes.getCustName().isEmpty()) {
			query = query + " AND ROOT_LEAD.CUST_NAME LIKE '%" + filterLeadRes.getCustName() + "%'";
		}

		if (filterLeadRes.getStatus() != null && !filterLeadRes.getStatus().isEmpty()) {
			query = query + " AND LEADS.STATUS = '" + filterLeadRes.getStatus() + "'";
		}

		if (filterLeadRes.getSalesRepId() != null) {
			query = query + " AND SALES_REP_ID = " + filterLeadRes.getSalesRepId();
		}

		if (filterLeadRes.getSalesRep() != null && !filterLeadRes.getSalesRep().isEmpty()) {
			query = query + " AND LEADS.SALES_REP LIKE '%" + filterLeadRes.getSalesRep() + "%'";
		}

		if (filterLeadRes.getStartDate() != null && filterLeadRes.getEndDate() != null) {
			query = query + " AND LEADS.CREATION_DATE BETWEEN '" + filterLeadRes.getStartDate() + "' AND '"
					+ filterLeadRes.getEndDate() + "'";
		} else if (filterLeadRes.getStartDate() != null) {
			query = query + " AND LEADS.CREATION_DATE >= '" + filterLeadRes.getStartDate() + "'";
		} else if (filterLeadRes.getEndDate() != null) {
			query = query + " AND LEADS.CREATION_DATE <= '" + filterLeadRes.getEndDate() + "'";
		}

		if (filterLeadRes.getFromBu() != null && !filterLeadRes.getFromBu().isEmpty()) {
			query = query + " AND LEADS.ORIGINATING_BU = '" + filterLeadRes.getFromBu() + "'";
		}

		if (filterLeadRes.getToBu() != null && !filterLeadRes.getToBu().isEmpty()) {
			query = query + " AND LEADS.BU = '" + filterLeadRes.getToBu() + "'";
		}

		if (filterLeadRes.getCreatorId() != null) {
			query = query + " AND LEADS.BU = " + filterLeadRes.getCreatorId();
		}

		return query;
	}

	@Override
	public LeadStatistictsRes getLeadStatistics(FilterLeadRes filterLeadRes, Boolean busummary, Long userId) {
		String query = "SELECT STATUS, COUNT(*) STATUS_COUNT FROM LEADS WHERE 1 = 1 ";

		query = getLeadStatisticsQuery(filterLeadRes, query);

		query = query + " GROUP BY STATUS";

		System.out.println(query);

		Map<String, Long> leadStatusCountMap = new HashMap<String, Long>();
		LeadStatistictsRes leadStatistictsRes = new LeadStatistictsRes();
		List<Map<String, Object>> statusCountRows = jdbcTemplate.queryForList(query);

		for (Map statusCountRow : statusCountRows) {
			leadStatusCountMap.put(((String) statusCountRow.get("STATUS")),
					((Long) statusCountRow.get("STATUS_COUNT")));
		}
		leadStatistictsRes.setLeadStatusCountMap(leadStatusCountMap);

		if (busummary && (userId != null)) {
			UserEntity userEntity = userDAO.getUserByUserId(userId);
			String salesRepBu = userEntity.getBusinessUnit();
			FilterLeadRes tempFilterLeadRes;
			List<Map<String, Object>> buLeadsRs;
			String mainQuery = "SELECT COUNT(*) COUNT FROM LEADS WHERE 1 = 1 ";

			// Internal BU Leads
			String tempQuery = mainQuery;
			tempFilterLeadRes = new FilterLeadRes();
			tempFilterLeadRes.setFromBu(salesRepBu);
			tempFilterLeadRes.setToBu(salesRepBu);
			tempQuery = getLeadStatisticsQuery(tempFilterLeadRes, tempQuery);
			buLeadsRs = jdbcTemplate.queryForList(tempQuery);
			for (Map statusCountRow : buLeadsRs) {
				leadStatistictsRes.setInternalBuLeadsCount((Long) statusCountRow.get("COUNT"));
			}

			// External BU Leads
			tempQuery = mainQuery;
			tempFilterLeadRes = new FilterLeadRes();
			tempFilterLeadRes.setFromBu(salesRepBu);
			tempQuery = getLeadStatisticsQuery(tempFilterLeadRes, tempQuery);
			buLeadsRs = jdbcTemplate.queryForList(tempQuery);
			for (Map statusCountRow : buLeadsRs) {
				leadStatistictsRes.setExternalBuLeadsCount((Long) statusCountRow.get("COUNT"));
			}

			// Cross BU Leads
			tempQuery = mainQuery;
			tempFilterLeadRes = new FilterLeadRes();
			tempFilterLeadRes.setToBu(salesRepBu);
			tempQuery = getLeadStatisticsQuery(tempFilterLeadRes, tempQuery);
			buLeadsRs = jdbcTemplate.queryForList(tempQuery);
			for (Map statusCountRow : buLeadsRs) {
				leadStatistictsRes.setCrossBuLeadsCount((Long) statusCountRow.get("COUNT"));
			}

			// Leads generated by user
			tempQuery = mainQuery;
			tempFilterLeadRes = new FilterLeadRes();
			tempFilterLeadRes.setCreatorId(userId);
			tempQuery = getLeadStatisticsQuery(tempFilterLeadRes, tempQuery);
			buLeadsRs = jdbcTemplate.queryForList(tempQuery);
			for (Map statusCountRow : buLeadsRs) {
				leadStatistictsRes.setTotalLeadsGeneratedByUser((Long) statusCountRow.get("COUNT"));
			}

		}

		return leadStatistictsRes;
	}

	@Override
	public boolean updateLeadAttachment(LeadEntity leadEntity) {
		String sql = "UPDATE LEADS SET ATTACHMENT = ?, UPDATE_DATE = ?,UPDATOR_ID = ? WHERE ID = ?;";
		int affectedRows = jdbcTemplate.update(sql, leadEntity.getAttachment(), leadEntity.getUpdateDate(),
				leadEntity.getUpdatorId(), leadEntity.getId());
		return affectedRows == 0 ? false : true;
	}

}
