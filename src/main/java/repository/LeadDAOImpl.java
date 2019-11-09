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
import org.springframework.context.annotation.Scope;
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
import repository.mapper.LeadRowMapper;
import security.SqlSafeUtil;

@Repository
@Scope("prototype")
public class LeadDAOImpl implements ILeadDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	// @Autowired
	// private IUserDAO userDAO;

	@Override
	public LeadEntity getLead(Long id) {
		String sql = "SELECT ID, BU,INDUSTRY,SALES_REP,STATUS,ROOT_ID,DELETED,CREATION_DATE,CREATOR_ID,UPDATE_DATE,UPDATOR_ID,BUDGET,CURRENCY,MESSAGE, SALES_REP_ID,ATTACHMENT FROM LEADS WHERE ID = ?";
		RowMapper<LeadEntity> rowMapper = new LeadRowMapper();
		return this.jdbcTemplate.queryForObject(sql, rowMapper, new Object[] { id });
	}

	@Override
	public List<LeadEntity> getLeadsByRoot(Long rootLeadId) {
		String sql = "SELECT ID, INDUSTRY, BU,SALES_REP,STATUS,ROOT_ID,DELETED,CREATION_DATE,CREATOR_ID,UPDATE_DATE,UPDATOR_ID,BUDGET,CURRENCY,MESSAGE, SALES_REP_ID,ATTACHMENT FROM LEADS WHERE ROOT_ID = ?";
		RowMapper<LeadEntity> rowMapper = new LeadRowMapper();
		return this.jdbcTemplate.query(sql, rowMapper, new Object[] { rootLeadId });
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
		String sql = "SELECT ID, BU,INDUSTRY, SALES_REP_ID,STATUS,ROOT_ID,DELETED,CREATION_DATE,CREATOR_ID,UPDATE_DATE,UPDATOR_ID,BUDGET,CURRENCY,MESSAGE,ATTACHMENT FROM LEADS WHERE 1 = 1";

		if (!SqlSafeUtil.isSqlInjectionSafe(leadtype)) {
			throw new RuntimeException("SQL Injection Error");
		}

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
		if (!SqlSafeUtil.isSqlInjectionSafe(description)) {
			throw new RuntimeException("SQL Injection Error");
		}

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

	private void checkSqlInjectionSafety(FilterLeadRes filterLeadRes) {
		if (!filterLeadRes.isSqlInjectionSafe()) {
			throw new RuntimeException("SQL Injection Error");
		}
	}

	@Override
	public List<LeadEntity> filterLeads(FilterLeadRes filterLeadRes, Pagination pagination) {
		RowMapper<LeadEntity> rowMapper = new LeadRowMapper();

		this.checkSqlInjectionSafety(filterLeadRes);

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

		if (filterLeadRes.getSearchText() != null && !filterLeadRes.getSearchText().isEmpty()) {
			query = query
					+ " AND CONCAT(ROOT_LEAD.CUST_NAME,' ',ROOT_LEAD.DESCRIPTION,' ',LEAD_CONTACT.NAME, ' ',LEAD_CONTACT.EMAIL, ' ',LEAD_CONTACT.PHONE) LIKE '%"
					+ filterLeadRes.getSearchText() + "%' ";
		}

		if (filterLeadRes.getCustName() != null && !filterLeadRes.getCustName().isEmpty()) {
			query = query + " AND ROOT_LEAD.CUST_NAME LIKE '%" + filterLeadRes.getCustName() + "%'";
		}

		if (filterLeadRes.getDescription() != null && !filterLeadRes.getDescription().isEmpty()) {
			query = query + " AND ROOT_LEAD.DESCRIPTION LIKE '%" + filterLeadRes.getDescription() + "%'";
		}

		if (filterLeadRes.getStatus() != null && !filterLeadRes.getStatus().isEmpty()) {
			query = query + " AND LEADS.STATUS = '" + filterLeadRes.getStatus() + "'";
		}

		if (filterLeadRes.getSalesRepId() != null) {
			query = query + " AND SALES_REP_ID = " + filterLeadRes.getSalesRepId();
		}

		// if (filterLeadRes.getSalesRep() != null &&
		// !filterLeadRes.getSalesRep().isEmpty()) {
		// query = query + " AND LEADS.SALES_REP LIKE '%" + filterLeadRes.getSalesRep()
		// + "%'";
		// }

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
			query = query + " AND LEADS.CREATOR_ID = " + filterLeadRes.getCreatorId();
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
		// if (filterLeadRes.getCustName() != null &&
		// !filterLeadRes.getCustName().isEmpty()) {
		// query = query + " AND ROOT_LEAD.CUST_NAME LIKE '%" +
		// filterLeadRes.getCustName() + "%'";
		// }

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
			query = query + " AND LEADS.CREATOR_ID = " + filterLeadRes.getCreatorId();
		}

		return query;
	}

	public LeadStatistictsRes getLeadStatistics(FilterLeadRes filterLeadRes, Boolean busummary, Long userId) {

		this.checkSqlInjectionSafety(filterLeadRes);

		String query = "SELECT ID, STATUS FROM LEADS WHERE 1 = 1 ";
		query = getLeadStatisticsQuery(filterLeadRes, query);
		List<Map<String, Object>> statusCountRows = jdbcTemplate.queryForList(query);
		Map<String, Long> leadStatusCountMap = new HashMap<String, Long>();
		LeadStatistictsRes leadStatistictsRes = new LeadStatistictsRes();
		for (Map statusCountRow : statusCountRows) {
			Long temlCount = new Long(0);
			if (statusCountRow.get("STATUS") != null) {
				if (leadStatusCountMap.get((String) statusCountRow.get("STATUS")) != null) {
					temlCount = leadStatusCountMap.get((String) statusCountRow.get("STATUS")) + 1;
					leadStatusCountMap.put((String) statusCountRow.get("STATUS"), temlCount);
				} else {
					leadStatusCountMap.put((String) statusCountRow.get("STATUS"), new Long(1));
				}
			}
		}
		leadStatistictsRes.setLeadStatusCountMap(leadStatusCountMap);
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
