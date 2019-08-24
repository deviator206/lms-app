package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;

import model.FilterMarketIntelligenceRes;
import repository.entity.LeadEntity;
import repository.entity.MarketIntelligenceEntity;
import repository.entity.MarketIntelligenceInfoEntity;
import repository.mapper.LeadRowMapper;
import repository.mapper.MarketIntelligenceInfoRowMapper;
import repository.mapper.MarketIntelligenceRowMapper;

@Repository
public class MarketIntelligenceDAO implements IMarketIntelligenceDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private PlatformTransactionManager transactionManager;

	@Override
	public List<MarketIntelligenceEntity> getMarketIntelligence() {
		String sql = "SELECT ID, TYPE, STATUS, NAME, DESCRIPTION, INVESTMENT,LEAD_ID, CREATION_DATE FROM MI";
		RowMapper<MarketIntelligenceEntity> rowMapper = new MarketIntelligenceRowMapper();
		return this.jdbcTemplate.query(sql, rowMapper);
	}

	@Override
	public MarketIntelligenceEntity getMarkByIdetIntelligenceById(Long id) {
		String sql = "SELECT ID, TYPE, STATUS, NAME, DESCRIPTION, INVESTMENT,LEAD_ID, CREATION_DATE FROM MI WHERE ID = ?";
		RowMapper<MarketIntelligenceEntity> rowMapper = new MarketIntelligenceRowMapper();
		MarketIntelligenceEntity miEntity = jdbcTemplate.queryForObject(sql, rowMapper, id);
		return miEntity;
	}

	@Override
	public void updateLeadInMarketIntelligence(Long rootLeadId, Long miId) {
		String sql = "UPDATE MI SET LEAD_ID = ? WHERE ID = ?;";
		jdbcTemplate.update(sql, rootLeadId, miId);
	}

	@Override
	public Long addMarketIntelligence(MarketIntelligenceEntity miEntity) {
		String sql = "INSERT INTO MI (TYPE, STATUS, NAME, DESCRIPTION, INVESTMENT, CREATION_DATE) VALUES (?,?,?,?,?,?) ";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, miEntity.getType());
				ps.setString(2, miEntity.getStatus());
				ps.setString(3, miEntity.getName());
				ps.setString(4, miEntity.getDescription());
				if (miEntity.getInvestment() != null) {
					ps.setDouble(5, miEntity.getInvestment());
				} else {
					ps.setNull(5, Types.DECIMAL);
				}
				ps.setDate(6,
						miEntity.getCreationDate() != null ? new java.sql.Date(miEntity.getCreationDate().getTime())
								: new java.sql.Date(new Date().getTime()));
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().longValue();
	}

	@Override
	public List<MarketIntelligenceInfoEntity> getMarketIntelligenceInfoList(Long miId) {
		String sql = "SELECT ID, MI_ID, INFO FROM MI_INFO WHERE MI_ID = ?";
		RowMapper<MarketIntelligenceInfoEntity> rowMapper = new MarketIntelligenceInfoRowMapper();
		List<MarketIntelligenceInfoEntity> miEntityLst = jdbcTemplate.query(sql, rowMapper, miId);
		return miEntityLst;
	}

	@Override
	public void addMarketIntelligenceInfo(Long miId, String info) {
		String sql = "INSERT INTO MI_INFO (MI_ID, INFO) VALUES (?,?)";
		jdbcTemplate.update(sql, miId, info);
	}

	@Override
	public void updateMarketIntelligence(MarketIntelligenceEntity marketingIntelligenceEntity) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<MarketIntelligenceEntity> filterMarketIntelligence(
			FilterMarketIntelligenceRes filterMarketIntelligence) {
		RowMapper<MarketIntelligenceEntity> rowMapper = new MarketIntelligenceRowMapper();

		String query = "SELECT ID, TYPE, STATUS, NAME, DESCRIPTION, INVESTMENT,LEAD_ID, CREATION_DATE FROM MI WHERE 1 = 1";

		if (filterMarketIntelligence.getName() != null && !filterMarketIntelligence.getName().isEmpty()) {
			query = query + " AND NAME LIKE '%" + filterMarketIntelligence.getName() + "%'";
		}

		if (filterMarketIntelligence.getType() != null && !filterMarketIntelligence.getType().isEmpty()) {
			query = query + " AND TYPE = '" + filterMarketIntelligence.getType() + "'";
		}

		if (filterMarketIntelligence.getInvestment() != null) {
			query = query + " AND INVESTMENT = '" + filterMarketIntelligence.getInvestment() + "'";
		}

		if (filterMarketIntelligence.getStartDate() != null && filterMarketIntelligence.getEndDate() != null) {
			query = query + " AND CREATION_DATE BETWEEN '" + filterMarketIntelligence.getStartDate() + "' AND '"
					+ filterMarketIntelligence.getEndDate() + "'";
		} else if (filterMarketIntelligence.getStartDate() != null) {
			query = query + " AND CREATION_DATE >= '" + filterMarketIntelligence.getStartDate() + "'";
		} else if (filterMarketIntelligence.getEndDate() != null) {
			query = query + " AND CREATION_DATE <= '" + filterMarketIntelligence.getEndDate() + "'";
		}
		System.out.println(query);

		return this.jdbcTemplate.query(query, rowMapper);
	}

}
