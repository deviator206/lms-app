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

import model.FilterLeadRes;
import model.LeadStatistictsRes;
import repository.entity.LeadEntity;
import repository.mapper.LeadRowMapper;

@Repository
public class LeadDAOImpl implements ILeadDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public LeadEntity getLead(Long id) {
		String sql = "SELECT ID, BU,SALES_REP,STATUS,ROOT_ID,DELETED,CREATION_DATE,CREATOR_ID,UPDATE_DATE,UPDATOR_ID,BUDGET,CURRENCY,MESSAGE FROM LEADS WHERE ID = ?";
		RowMapper<LeadEntity> rowMapper = new LeadRowMapper();
		return this.jdbcTemplate.queryForObject(sql, rowMapper, new Object[] { id });
	}

	@Override
	public Long insertLead(LeadEntity leadEntity) {
		Long id = null;
		String sql = "INSERT INTO LEADS (BU,STATUS,ROOT_ID,DELETED,CREATION_DATE,CREATOR_ID,UPDATE_DATE,UPDATOR_ID,SALES_REP,BUDGET,CURRENCY,MESSAGE,ORIGINATING_BU,SALES_REP_ID) \r\n"
				+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
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
	public List<LeadEntity> getLeads() {
		String sql = "SELECT ID, BU,SALES_REP,STATUS,ROOT_ID,DELETED,CREATION_DATE,CREATOR_ID,UPDATE_DATE,UPDATOR_ID,BUDGET,CURRENCY,MESSAGE FROM LEADS";
		RowMapper<LeadEntity> rowMapper = new LeadRowMapper();
		return this.jdbcTemplate.query(sql, rowMapper);
	}

	@Override
	public List<LeadEntity> searchLeads(String name, String description) {
		// String sql = "SELECT ID,
		// BU,SALES_REP,STATUS,ROOT_ID,DELETED,CREATION_DATE,CREATOR_ID,UPDATE_DATE,UPDATOR_ID,BUDGET,CURRENCY,MESSAGE
		// FROM LEADS WHERE DESCRIPTION LIKE %?%' ?";

		RowMapper<LeadEntity> rowMapper = new LeadRowMapper();
		// return this.jdbcTemplate.query(sql, rowMapper, new Object[] { description });

		String sql = "SELECT LEADS.ID, LEADS.BU,LEADS.SALES_REP,LEADS.STATUS,LEADS.ROOT_ID,LEADS.DELETED,LEADS.CREATION_DATE,LEADS.CREATOR_ID,LEADS.UPDATE_DATE,LEADS.UPDATOR_ID,LEADS.BUDGET,LEADS.CURRENCY,LEADS.MESSAGE FROM LEADS,ROOT_LEAD WHERE LEADS.ROOT_ID = ROOT_LEAD.ID AND ROOT_LEAD.DESCRIPTION LIKE ?";
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
	public List<LeadEntity> filterLeads(FilterLeadRes filterLeadRes) {
		RowMapper<LeadEntity> rowMapper = new LeadRowMapper();

		String query = "SELECT LEADS.ID, LEADS.BU,LEADS.SALES_REP,LEADS.STATUS,LEADS.ROOT_ID,LEADS.DELETED,LEADS.CREATION_DATE,LEADS.CREATOR_ID,LEADS.UPDATE_DATE,LEADS.UPDATOR_ID,LEADS.BUDGET,LEADS.CURRENCY,LEADS.MESSAGE FROM LEADS,ROOT_LEAD WHERE LEADS.ROOT_ID = ROOT_LEAD.ID ";

		if (filterLeadRes.getCustName() != null && !filterLeadRes.getCustName().isEmpty()) {
			query = query + " AND ROOT_LEAD.CUST_NAME LIKE '%" + filterLeadRes.getCustName() + "%'";
		}

		if (filterLeadRes.getStatus() != null && !filterLeadRes.getStatus().isEmpty()) {
			query = query + " AND LEADS.STATUS = '" + filterLeadRes.getStatus() + "'";
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

		System.out.println(query);

		return this.jdbcTemplate.query(query, rowMapper);
	}

	@Override
	public LeadStatistictsRes getLeadStatistics() {
		String sql = "SELECT STATUS, COUNT(*) STATUS_COUNT FROM LEADS GROUP BY STATUS;";
		Map<String, Long> leadStatusCountMap = new HashMap<String, Long>();
		LeadStatistictsRes leadStatistictsRes = new LeadStatistictsRes();
		List<Map<String, Object>> statusCountRows = jdbcTemplate.queryForList(sql);

		for (Map statusCountRow : statusCountRows) {
			leadStatusCountMap.put(((String) statusCountRow.get("STATUS")),
					((Long) statusCountRow.get("STATUS_COUNT")));
		}
		leadStatistictsRes.setLeadStatusCountMap(leadStatusCountMap);
		return leadStatistictsRes;
	}

}
